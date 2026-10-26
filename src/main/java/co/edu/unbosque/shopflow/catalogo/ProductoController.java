package co.edu.unbosque.shopflow.catalogo;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ServicioCatalogo catalogo;

    public ProductoController(ServicioCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    @GetMapping
    public List<Producto> listar() {
        return catalogo.listar();
    }

    @GetMapping("/{sku}")
    public Producto buscar(@PathVariable String sku) {
        return catalogo.buscar(sku);
    }

    @PutMapping("/{sku}/precio")
    public Producto cambiarPrecio(@PathVariable String sku,
                                  @RequestHeader(value = "X-Rol", required = false) String rol,
                                  @RequestBody CambioPrecio body) {
        if (body.precio() == null || body.precio().signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");   // → 400
        }
        // AQUÍ entra el patrón cuando exista:
        // ServicioCatalogo protegido = new CatalogoProteccionProxy(catalogo, rol);
        // return protegido.actualizarPrecio(sku, body.precio());
        return catalogo.actualizarPrecio(sku, body.precio());
    }

    public record CambioPrecio(BigDecimal precio) { }
}