package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraIgvPeru implements CalculadoraImpuestos {

    private static final BigDecimal TASA = new BigDecimal("0.18");

    @Override
    public String nombreImpuesto() {
        return "IGV 18%";
    }

    @Override
    public BigDecimal calcular(BigDecimal base) {
        return base.multiply(TASA).setScale(2, RoundingMode.HALF_UP);
    }
}
