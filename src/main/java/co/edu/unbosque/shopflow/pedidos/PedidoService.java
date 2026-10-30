package co.edu.unbosque.shopflow.pedidos;

import co.edu.unbosque.shopflow.catalogo.Producto;
import co.edu.unbosque.shopflow.catalogo.ServicioCatalogo;
import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.pagos.PasarelaDePago;
import co.edu.unbosque.shopflow.pagos.ResultadoPago;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    // Este Map es el "repositorio": no hay base de datos
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    private final ServicioCatalogo catalogo;
    private final ServicioFacturacion facturacion;
    private final Map<String, PasarelaDePago> pasarelas;

    public PedidoService(ServicioCatalogo catalogo,
                         ServicioFacturacion facturacion,
                         Map<String, PasarelaDePago> pasarelas) {
        this.catalogo = catalogo;
        this.facturacion = facturacion;
        this.pasarelas = pasarelas;
    }

    /** Director del Builder: traduce el JSON a llamadas al builder, factura y guarda. */
    public Pedido crear(PedidoRequest request) {
        Pedido.Builder builder = new Pedido.Builder(
                request.clienteId(), request.documentoCliente(), request.pais(), request.contacto())
                .id("PED-" + secuencia.incrementAndGet())
                .direccionEnvio(request.direccionEnvio())
                .recogerEnTienda(Boolean.TRUE.equals(request.recogerEnTienda()))
                .cupon(request.cupon())
                .notaRegalo(request.notaRegalo())
                .envioExpress(Boolean.TRUE.equals(request.envioExpress()))
                .canalNotificacion(request.canalNotificacion());

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

        ResultadoPago resultado = elegirPasarela(proveedor).cobrar(pedido.getTotal(), pedido.getId());
        if (!resultado.aprobado()) {
            throw new IllegalStateException(resultado.mensaje());   // → 409
        }

        Pedido pagado = pedido.conPagoAprobado(resultado.autorizacion());
        pedidos.put(id, pagado);
        // FACTORY METHOD: quien tenga Notificaciones agrega aquí la notificación del pago aprobado.
        return pagado;
    }

    private PasarelaDePago elegirPasarela(String proveedor) {
        return Optional.ofNullable(proveedor)
                .map(p -> p.trim().toUpperCase(Locale.ROOT))
                .map(pasarelas::get)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Proveedor de pago no válido: " + proveedor + ". Use LEGACYPAY o QUICKPAY"));   // → 400
    }
}
