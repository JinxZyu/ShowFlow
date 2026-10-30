package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MetricasPagoTest {

    @Test
    void cuentaCobrosYAprobados() {
        int[] llamada = {0};
        PasarelaDePago falsa = (monto, ref) ->                 // aprueba, aprueba, rechaza
                ++llamada[0] < 3 ? ResultadoPago.aprobado("AUT") : ResultadoPago.rechazado("Fondos insuficientes");
        MetricasPago metricas = new MetricasPago(falsa, "FALSA");

        metricas.cobrar(BigDecimal.TEN, "PED-1");
        metricas.cobrar(BigDecimal.TEN, "PED-2");
        metricas.cobrar(BigDecimal.TEN, "PED-3");

        assertEquals(3, metricas.cobros());
        assertEquals(2, metricas.aprobados());
        assertEquals(66.7, metricas.tasaAprobacion(), 0.1);
    }

    @Test
    void unaCaidaCuentaComoCobroNoAprobadoYSigueLanzandose() {
        PasarelaDePago caida = (monto, ref) -> { throw new PasarelaNoDisponibleException("caída", null); };
        MetricasPago metricas = new MetricasPago(caida, "FALSA");

        assertThrows(PasarelaNoDisponibleException.class,
                () -> metricas.cobrar(BigDecimal.TEN, "PED-1"));
        assertEquals(1, metricas.cobros());
        assertEquals(0, metricas.aprobados());
    }

    @Test
    void sinCobrosLaTasaEsCero() {
        MetricasPago metricas = new MetricasPago((monto, ref) -> ResultadoPago.aprobado("AUT"), "FALSA");

        assertEquals(0.0, metricas.tasaAprobacion());
    }
}
