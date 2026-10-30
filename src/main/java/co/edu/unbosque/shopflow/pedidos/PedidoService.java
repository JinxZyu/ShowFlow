package co.edu.unbosque.shopflow.pedidos;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import co.edu.unbosque.shopflow.catalogo.ServicioCatalogo;
import co.edu.unbosque.shopflow.pagos.ResultadoPago;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    // Este Map es el "repositorio": no hay base de datos
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    private final ServicioCatalogo catalogo;
    private final ServicioFacturacion facturacion;
    private final Map<String, Notificador> notificadores;

    public PedidoService(ServicioCatalogo catalogo,
                         ServicioFacturacion facturacion,
                         List<Notificador> notificadores) {
        this.catalogo = catalogo;
        this.facturacion = facturacion;
        this.notificadores = notificadores.stream()
                .collect(Collectors.toUnmodifiableMap(Notificador::canal, Function.identity()));
    }

    /** Director del Builder: traduce el JSON recibido a llamadas al builder, factura y guarda. */
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

    /** PENDIENTE (Adapter + Decorator): elegir la pasarela por proveedor y cobrar el total con impuestos. */
    private ResultadoPago cobrar(Pedido pedido, String proveedor) {
        throw new UnsupportedOperationException("Pendiente: Adapter / Decorator");
    }

    /** Usa el creador del canal elegido; no sabe qué notificación concreta se crea. */
    private void notificarPagoAprobado(Pedido pedido) {
        Optional.ofNullable(pedido.getCanalNotificacion())
                .map(notificadores::get)
                .ifPresent(n -> n.notificarPagoAprobado(
                        pedido.getContacto(), pedido.getId(), pedido.getComprobante().total()));
    }

    /** Rechaza (400) un canal que no tiene creador registrado. */
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