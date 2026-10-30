package co.edu.unbosque.shopflow.pagos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ReintentoPagoTest {
    @Test
    void reintentaHastaQueLaPasarelaResponde() {
        int[] llamadas = {0};
        PasarelaDePago inestable = (monto, ref) -> {          // pasarela falsa: falla 2 veces, aprueba la 3.ª
            if (++llamadas[0] < 3) throw new PasarelaNoDisponibleException("caída", null);
            return ResultadoPago.aprobado("AUT-1");
        };

        ResultadoPago r = new ReintentoPago(inestable, 3).cobrar(BigDecimal.TEN, "PED-1");

        assertTrue(r.aprobado());
        assertEquals(3, llamadas[0]);
    }

    @Test
    void siSeAgotanLosIntentosLanzaLaExcepcion() {
        int[] llamadas = {0};
        PasarelaDePago siempreCaida = (monto, ref) -> {
            llamadas[0]++;
            throw new PasarelaNoDisponibleException("caída", null);
        };

        assertThrows(PasarelaNoDisponibleException.class,
                () -> new ReintentoPago(siempreCaida, 3).cobrar(BigDecimal.TEN, "PED-1"));
        assertEquals(3, llamadas[0]);
    }

    @Test
    void unRechazoNoSeReintenta() {
        int[] llamadas = {0};
        PasarelaDePago rechaza = (monto, ref) -> {
            llamadas[0]++;
            return ResultadoPago.rechazado("Fondos insuficientes");
        };

        ResultadoPago r = new ReintentoPago(rechaza, 3).cobrar(BigDecimal.TEN, "PED-1");

        assertFalse(r.aprobado());
        assertEquals(1, llamadas[0]);
    }
}
