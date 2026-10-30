package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** ConcreteProduct A (Colombia): IVA del 19 %. */
public class CalculadoraIvaColombia implements CalculadoraImpuestos {

    private static final BigDecimal TASA = new BigDecimal("0.19");

    @Override
    public String nombreImpuesto() {
        return "IVA 19%";
    }

    @Override
    public BigDecimal calcular(BigDecimal base) {
        return base.multiply(TASA).setScale(2, RoundingMode.HALF_UP);
    }
}
