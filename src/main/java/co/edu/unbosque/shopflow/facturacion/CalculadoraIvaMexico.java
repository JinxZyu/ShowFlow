package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** ConcreteProduct A (México): IVA del 16 %. */
public class CalculadoraIvaMexico implements CalculadoraImpuestos {

    private static final BigDecimal TASA = new BigDecimal("0.16");

    @Override
    public String nombreImpuesto() {
        return "IVA 16%";
    }

    @Override
    public BigDecimal calcular(BigDecimal base) {
        return base.multiply(TASA).setScale(2, RoundingMode.HALF_UP);
    }
}
