package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LegacyPayAdapterTest {

    @Test
    void siLegacyPayEstaCaidoLanzaPasarelaNoDisponible() {
        PasarelaDePago adapter = new LegacyPayAdapter(new LegacyPayClient(1.0));   // siempre falla

        assertThrows(PasarelaNoDisponibleException.class,
                () -> adapter.cobrar(BigDecimal.TEN, "PED-1"));
    }

    @Test
    void masDeDosMilDolaresEsFondosInsuficientes() {
        PasarelaDePago adapter = new LegacyPayAdapter(new LegacyPayClient(0.0));   // nunca falla

        ResultadoPago resultado = adapter.cobrar(new BigDecimal("2500.00"), "PED-1");

        assertFalse(resultado.aprobado());
        assertEquals("Fondos insuficientes", resultado.mensaje());
    }

    @Test
    void montoNormalSeApruebaConAutorizacion() {
        PasarelaDePago adapter = new LegacyPayAdapter(new LegacyPayClient(0.0));

        ResultadoPago resultado = adapter.cobrar(new BigDecimal("145.66"), "PED-1");

        assertTrue(resultado.aprobado());
        assertTrue(resultado.autorizacion().startsWith("LP"));
    }
}