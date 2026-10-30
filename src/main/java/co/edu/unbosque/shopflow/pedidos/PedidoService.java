package co.edu.unbosque.shopflow.pedidos;

import co.edu.unbosque.shopflow.catalogo.Producto;
import co.edu.unbosque.shopflow.catalogo.ServicioCatalogo;
import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.notificaciones.Notificador;
import co.edu.unbosque.shopflow.pagos.PasarelaDePago;
import co.edu.unbosque.shopflow.pagos.ResultadoPago;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    private final ServicioCatalogo catalogo;
    private final ServicioFacturacion facturacion;
    private final Map<String, Notificador> notificadores;
    private final Map<String, PasarelaDePago> pasarelas;   // las arma ConfiguracionPagos (adapters + decoradores)

    public PedidoService(ServicioCatalogo catalogo,
                         ServicioFacturacion facturacion,
                         List<Notificador> notificadores,
                         Map<String, PasarelaDePago> pasarelas) {
        this.catalogo = catalogo;
        this.facturacion = facturacion;
        this.notificadores = notificadores.stream()
                .collect(Collectors.toUnmodifiableMap(Notificador::canal, Function.identity()));
        this.pasarelas = pasarelas;
    }

    public Pedido crear(PedidoRequest request) {
        Pedido.Builder builder = new Pedido.Builder(
                request.clienteId(), request.documentoCliente(), request.pais(), request.contacto())
                .id("PED-" + secuencia.incrementAndGet())
                .direccionEnvio(request.direccionEnvio())
                .recogerEnTienda(Boolean.TRUE.equals(request.recogerEnTienda()))
                .cupon(request.cupon())
                .notaRegalo(request.notaRegalo())
                .envioExpress(Boolean.TRUE.equals(request.envioExpress()))
                .canalNotificacion(validarCanal(request.canalNotificacion()));

        if (request.items() != null) {
            for (PedidoRequest.ItemRequest item : request.items()) {
                Producto producto = catalogo.buscar(item.sku());   // el precio sale del catálogo
                builder.item(new ItemPedido(producto.sku(), producto.nombre(), item.cantidad(), producto.precio()));
            }
        }

        Pedido pedido = builder.build();
        Pedido facturado = pedido.conComprobante(facturacion.facturar(pedido));
        pedidos.put(facturado.getId(), facturado);
        return facturado;
    }

    public Pedido buscar(String id) {
        Pedido pedido = pedidos.get(id);
        if (pedido == null) {
            throw new NoSuchElementException("No existe el pedido " + id);   // → 404
        }
        return pedido;
    }

    public Pedido pagar(String id, String proveedor) {
        Pedido pedido = buscar(id);
        if (pedido.getEstado() == Pedido.Estado.PAGADO) {
            throw new IllegalStateException("El pedido " + id + " ya está pagado");   // → 409
        }

        ResultadoPago resultado = cobrar(pedido, proveedor);
        if (!resultado.aprobado()) {
            throw new IllegalStateException(resultado.mensaje());   // → 409
        }

        Pedido pagado = pedido.conPagoAprobado(resultado.autorizacion());
        pedidos.put(id, pagado);
        notificarPagoAprobado(pagado);
        return pagado;
    }

    private ResultadoPago cobrar(Pedido pedido, String proveedor) {
        PasarelaDePago pasarela = (proveedor == null) ? null
                : pasarelas.get(proveedor.trim().toUpperCase(Locale.ROOT));
        if (pasarela == null) {
            throw new IllegalArgumentException("Proveedor no soportado: " + proveedor);   // → 400
        }
        return pasarela.cobrar(pedido.getTotal(), pedido.getId());
    }

    private void notificarPagoAprobado(Pedido pedido) {
        Optional.ofNullable(pedido.getCanalNotificacion())
                .map(notificadores::get)
                .ifPresent(n -> n.notificarPagoAprobado(
                        pedido.getContacto(), pedido.getId(), pedido.getComprobante().total()));
    }

    private String validarCanal(String canal) {
        if (canal == null || canal.isBlank()) {
            return null;
        }
        String normalizado = canal.trim().toUpperCase(Locale.ROOT);
        if (!notificadores.containsKey(normalizado)) {
            throw new IllegalArgumentException(
                    "Canal de notificación no soportado: " + canal + ". Disponibles: " + notificadores.keySet());
        }
        return normalizado;
    }
}