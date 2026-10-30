package co.edu.unbosque.shopflow.catalogo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CatalogoCacheProxy implements ServicioCatalogo {

    private final ServicioCatalogo real;
    private final Map<String, Producto> cache = new ConcurrentHashMap<>();

    public CatalogoCacheProxy(ServicioCatalogo real) {
        this.real = real;
    }

    @Override
    public Producto buscar(String sku) {
        Producto enCache = cache.get(sku);
        if (enCache != null) {
            return enCache;
        }
        Producto producto = real.buscar(sku);
        cache.put(sku, producto);
        return producto;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> productos = real.listar();
        productos.forEach(p -> cache.put(p.sku(), p));
        return productos;
    }

    @Override
    public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
        Producto actualizado = real.actualizarPrecio(sku, nuevoPrecio);
        cache.put(sku, actualizado);
        return actualizado;
    }
}
