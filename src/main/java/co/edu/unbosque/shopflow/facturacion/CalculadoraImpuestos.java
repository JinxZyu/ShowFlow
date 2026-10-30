package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

public interface CalculadoraImpuestos {
    String nombreImpuesto();
    BigDecimal calcular(BigDecimal base);
}
