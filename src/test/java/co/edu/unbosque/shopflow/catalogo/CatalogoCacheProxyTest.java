
package co.edu.unbosque.shopflow.catalogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogoCacheProxyTest {

    private ServicioCatalogo sujetoReal;
    private CatalogoCacheProxy proxy;
    private Producto producto;

    @BeforeEach
    void configurar() {

        sujetoReal = mock(ServicioCatalogo.class);

        proxy = new CatalogoCacheProxy(sujetoReal);

        producto = new Producto(
            "SKU-001",
            "Teclado mecanico",
            new BigDecimal("85.00")
        );
    }

    @Test
    void primeraBusquedaConsultaAlCatalogoReal() {

        when(sujetoReal.buscar("SKU-001"))
                .thenReturn(producto);

        Producto resultado = proxy.buscar("SKU-001");

        assertEquals(producto, resultado);

        verify(sujetoReal, times(1))
                .buscar("SKU-001");
    }

    @Test
    void segundaBusquedaUtilizaLaCache() {

        when(sujetoReal.buscar("SKU-001"))
                .thenReturn(producto);

        Producto primeraBusqueda = proxy.buscar("SKU-001");
        Producto segundaBusqueda = proxy.buscar("SKU-001");

        assertSame(primeraBusqueda, segundaBusqueda);

        verify(sujetoReal, times(1))
                .buscar("SKU-001");
    }

    @Test
    void buscarOtroProductoConsultaNuevamenteAlCatalogo() {

        Producto otroProducto = new Producto(
            "SKU-002",
            "Mouse inalambrico",
            new BigDecimal("25.50")
        );

        when(sujetoReal.buscar("SKU-001"))
                .thenReturn(producto);

        when(sujetoReal.buscar("SKU-002"))
                .thenReturn(otroProducto);

        proxy.buscar("SKU-001");
        proxy.buscar("SKU-002");

        verify(sujetoReal, times(1)).buscar("SKU-001");
        verify(sujetoReal, times(1)).buscar("SKU-002");
    }
}
