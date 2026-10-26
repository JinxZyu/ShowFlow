package co.edu.unbosque.shopflow.pedidos;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    // Este Map es el "repositorio": no hay base de datos
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    public Pedido crear(PedidoRequest request) {
        // PENDIENTE (Builder + Abstract Factory): traducir el request a new Pedido.Builder(...)...build(),
        // facturar, guardar en el Map y devolverlo
        throw new UnsupportedOperationException("Pendiente: Builder");
    }



    public Pedido buscar(String id) {
        Pedido pedido = pedidos.get(id);
        if (pedido == null) {
            throw new NoSuchElementException("No existe el pedido " + id);   // → 404
        }
        return pedido;
    }

    public Pedido pagar(String id, String proveedor) {
        // PENDIENTE (Adapter + Decorator + Factory Method): elegir la pasarela, cobrar,
        // marcar como pagado y notificar
        throw new UnsupportedOperationException("Pendiente: Adapter / Decorator");
    }
}
