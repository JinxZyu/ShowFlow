package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

/** AbstractProduct A: cálculo del impuesto del país. */
public interface CalculadoraImpuestos {
    String nombreImpuesto();
    BigDecimal calcular(BigDecimal base);
}

