package co.edu.unbosque.shopflow.catalogo.proxy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.edu.unbosque.shopflow.catalogo.domein.Producto;
import co.edu.unbosque.shopflow.catalogo.domein.ServicioCatalogo;


public class CatalogoCacheProxy implements ServicioCatalogo {

    private final ServicioCatalogo sujetoReal;
    private final Map<String, Producto> cache = new ConcurrentHashMap<>();

    public CatalogoCacheProxy(ServicioCatalogo sujetoReal) {
        this.sujetoReal = sujetoReal;
    }

    @Override
    public Producto buscar(String sku) {
        Producto enCache = cache.get(sku);
        if (enCache != null) {
            return enCache;
        }
        Producto p = sujetoReal.buscar(sku);   
        cache.put(sku, p);
        return p;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> productos = sujetoReal.listar();
        productos.forEach(p -> cache.put(p.sku(), p));
        return productos;
    }

    @Override
    public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
        Producto actualizado = sujetoReal.actualizarPrecio(sku, nuevoPrecio);
        cache.put(sku, actualizado);          
        return actualizado;
    }
}