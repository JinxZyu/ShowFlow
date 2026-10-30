package co.edu.unbosque.shopflow.pedidos;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import co.edu.unbosque.shopflow.pagos.PasarelaDePago;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    // Este Map es el "repositorio": no hay base de datos
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
    private final Map<String, PasarelaDePago> pasarelas;

    public PedidoService(Map<String, PasarelaDePago> pasarelas){
        this.pasarelas=pasarelas;
    }

    public Pedido crear(PedidoRequest request) {
        // PENDIENTE (Builder + Abstract Factory)
        throw new UnsupportedOperationException("Pendiente: Builder");
    }

    public Pedido buscar(String id) {
        Pedido pedido = pedidos.get(id);
        if (pedido == null) {
            throw new NoSuchElementException("No existe el pedido " + id);
        }
        return pedido;
    }

    public Pedido pagar(String id, String proveedor) {
        // 3. PENDIENTE: cuando exista el Pedido del Builder, reemplazar el throw por:
        //
        //    if (pedido ya está PAGADO) throw new IllegalStateException("El pedido ya fue pagado");   // → 409
        //    ResultadoPago resultado = pasarela.cobrar(pedido.getTotal(), pedido.getId());
        //    if (!resultado.aprobado()) throw new IllegalStateException(resultado.mensaje());          // → 409
        //    guardar el pedido como PAGADO con resultado.autorizacion() y devolverlo
        throw new UnsupportedOperationException("Pendiente: Adapter / Decorator");
    }
}
