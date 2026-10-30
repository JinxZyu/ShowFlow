package co.edu.unbosque.shopflow.facturacion;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FacturacionPeruTest {

    @Test
    void debeCalcularIgvDel18PorCiento() {
        CalculadoraIgvPeru calculadora = new CalculadoraIgvPeru();

        BigDecimal impuesto = calculadora.calcular(
                new BigDecimal("100.00")
        );

        assertEquals(new BigDecimal("18.00"), impuesto);
    }

    @Test
    void debeIdentificarElImpuestoComoIgv18() {
        CalculadoraIgvPeru calculadora = new CalculadoraIgvPeru();

        assertEquals("IGV 18%", calculadora.nombreImpuesto());
    }

    @Test
    void debeAceptarRucDeOnceDigitos() {
        ValidadorRucPeru validador = new ValidadorRucPeru();

        assertDoesNotThrow(() -> validador.validar("20123456789"));
    }

    @Test
    void debeRechazarRucDeDiezDigitos() {
        ValidadorRucPeru validador = new ValidadorRucPeru();

        assertThrows(
                IllegalArgumentException.class,
                () -> validador.validar("2012345678")
        );
    }

    @Test
    void debeRechazarRucDeDoceDigitos() {
        ValidadorRucPeru validador = new ValidadorRucPeru();

        assertThrows(
                IllegalArgumentException.class,
                () -> validador.validar("201234567890")
        );
    }

    @Test
    void debeRechazarRucNulo() {
        ValidadorRucPeru validador = new ValidadorRucPeru();

        assertThrows(
                IllegalArgumentException.class,
                () -> validador.validar(null)
        );
    }

    @Test
    void debeGenerarFacturaElectronicaSunat() {
        GeneradorFacturaSunat generador = new GeneradorFacturaSunat();

        Comprobante comprobante = generador.generar(
                "PED-1",
                new BigDecimal("100.00"),
                "IGV 18%",
                new BigDecimal("18.00")
        );

        assertEquals("Factura electrónica (SUNAT)", comprobante.tipo());
        assertEquals("FE-PE-1", comprobante.numero());
    }

    @Test
    void debeCalcularCorrectamenteElTotalDeLaFactura() {
        GeneradorFacturaSunat generador = new GeneradorFacturaSunat();

        Comprobante comprobante = generador.generar(
                "PED-1",
                new BigDecimal("100.00"),
                "IGV 18%",
                new BigDecimal("18.00")
        );

        assertEquals(new BigDecimal("100.00"), comprobante.base());
        assertEquals(new BigDecimal("18.00"), comprobante.impuesto());
        assertEquals(new BigDecimal("118.00"), comprobante.total());
    }

    @Test
    void debeCrearLaFamiliaDeFacturacionPeruana() {
        FabricaPeru fabrica = new FabricaPeru();

        assertEquals("PE", fabrica.pais());

        assertInstanceOf(
                CalculadoraIgvPeru.class,
                fabrica.crearCalculadora()
        );

        assertInstanceOf(
                ValidadorRucPeru.class,
                fabrica.crearValidador()
        );

        assertInstanceOf(
                GeneradorFacturaSunat.class,
                fabrica.crearGenerador()
        );
    }
}