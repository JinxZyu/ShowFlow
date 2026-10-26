package co.edu.unbosque.shopflow.pedidos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService servicio;

    public PedidoController(PedidoService servicio) {
        this.servicio = servicio;
    }

    // POST /api/pedidos  → 201 Created
    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(request));
    }

    // GET /api/pedidos/{id}  → 200 o 404
    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable String id) {
        return servicio.buscar(id);
    }

    // POST /api/pedidos/{id}/pago  con {"proveedor": "LEGACYPAY"} o {"proveedor": "QUICKPAY"}
    @PostMapping("/{id}/pago")
    public Pedido pagar(@PathVariable String id, @RequestBody SolicitudPago body) {
        return servicio.pagar(id, body.proveedor());
    }

    public record SolicitudPago(String proveedor) { }
}
