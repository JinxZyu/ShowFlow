package co.edu.unbosque.shopflow.pedidos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoBuilderTest {

    private static ItemPedido item(String sku, int cantidad, String precio) {
        return new ItemPedido(sku, "Producto " + sku, cantidad, new BigDecimal(precio));
    }

    @Test
    void construyeUnPedidoConLosDatosDados() {
        Pedido pedido = new Pedido.Builder("CLI-1", "1017123456", "co", "cliente@correo.com")
                .direccionEnvio("Calle 1 # 2-3")
                .item(item("SKU-001", 2, "85.00"))
                .build();

        assertEquals("CLI-1", pedido.getClienteId());
        assertEquals("CO", pedido.getPais());
        assertEquals(1, pedido.getItems().size());
        assertEquals(new BigDecimal("170.00"), pedido.getSubtotal());
    }

    @Test
    void elCuponBosque10AplicaUnDescuentoDelDiezPorCiento() {
        Pedido pedido = new Pedido.Builder("CLI-1", "1017123456", "CO", "cliente@correo.com")
                .direccionEnvio("Calle 1 # 2-3")
                .item(item("SKU-001", 1, "100.00"))
                .cupon("bosque10")
                .build();

        assertEquals(new BigDecimal("10.00"), pedido.getDescuento());
        assertEquals(new BigDecimal("90.00"), pedido.getTotal());
    }

    @Test
    void sinItemsNoSePuedeConstruir() {
        Pedido.Builder builder = new Pedido.Builder("CLI-1", "1017123456", "CO", "cliente@correo.com")
                .direccionEnvio("Calle 1 # 2-3");

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void sinDireccionNoSePuedeEnviar() {
        Pedido.Builder builder = new Pedido.Builder("CLI-1", "1017123456", "CO", "cliente@correo.com")
                .item(item("SKU-001", 1, "50.00"));

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void recogerEnTiendaNoPuedeSerExpress() {
        Pedido.Builder builder = new Pedido.Builder("CLI-1", "1017123456", "CO", "cliente@correo.com")
                .item(item("SKU-001", 1, "50.00"))
                .recogerEnTienda(true)
                .envioExpress(true);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void cuponInvalidoLanzaExcepcion() {
        Pedido.Builder builder = new Pedido.Builder("CLI-1", "1017123456", "CO", "cliente@correo.com")
                .direccionEnvio("Calle 1 # 2-3")
                .item(item("SKU-001", 1, "50.00"))
                .cupon("FALSO");

        assertThrows(IllegalArgumentException.class, builder::build);
    }
}