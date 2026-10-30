package co.edu.unbosque.shopflow.pagos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AuditoriaPagoTest {
    @Test
    void registraUnaEntradaPorCobro() {
        RegistroAuditoria registro = new RegistroAuditoria();
        PasarelaDePago aprueba = (monto, ref) -> ResultadoPago.aprobado("AUT-1");

        new AuditoriaPago(aprueba, "FALSA", registro).cobrar(new BigDecimal("50.00"), "PED-1");

        assertEquals(1, registro.todas().size());
        RegistroAuditoria.EntradaAuditoria entrada = registro.todas().get(0);
        assertEquals("FALSA", entrada.proveedor());
        assertEquals(new BigDecimal("50.00"), entrada.monto());
        assertTrue(entrada.aprobado());
    }

    @Test
    void siLaPasarelaFallaRegistraYDejaPasarLaExcepcion() {
        RegistroAuditoria registro = new RegistroAuditoria();
        PasarelaDePago caida = (monto, ref) -> { throw new PasarelaNoDisponibleException("caída", null); };

        assertThrows(PasarelaNoDisponibleException.class,
                () -> new AuditoriaPago(caida, "FALSA", registro).cobrar(BigDecimal.TEN, "PED-1"));
        assertEquals(1, registro.todas().size());
        assertFalse(registro.todas().get(0).aprobado());
    }
}
