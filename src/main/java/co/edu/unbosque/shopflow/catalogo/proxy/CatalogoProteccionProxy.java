package co.edu.unbosque.shopflow.catalogo.proxy;


import co.edu.unbosque.shopflow.catalogo.domein.Producto;
import co.edu.unbosque.shopflow.catalogo.domein.ServicioCatalogo;
import co.edu.unbosque.shopflow.comun.AccesoDenegadoException;
import java.math.BigDecimal;
import java.util.List;


public class CatalogoProteccionProxy implements ServicioCatalogo {

    private final ServicioCatalogo sujetoReal;
    private final String rol;

    public CatalogoProteccionProxy(ServicioCatalogo sujetoReal, String rol) {
        this.sujetoReal = sujetoReal;
        this.rol = rol;
    }

    @Override
    public Producto buscar(String sku) {
        return sujetoReal.buscar(sku);
    }

    @Override
    public List<Producto> listar() {
        return sujetoReal.listar();
    }

    @Override
    public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
        if (!"ADMIN".equals(rol)) {            
            throw new AccesoDenegadoException("Solo el ADMIN puede cambiar precios");
        }
        return sujetoReal.actualizarPrecio(sku, nuevoPrecio);
    }
}