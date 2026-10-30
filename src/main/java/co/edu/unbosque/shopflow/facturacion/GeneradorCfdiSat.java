package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

/** ConcreteProduct C (México): comprobante CFDI del SAT. */
public class GeneradorCfdiSat implements GeneradorComprobante {

    @Override
    public Comprobante generar(String idPedido, BigDecimal base, String nombreImpuesto, BigDecimal impuesto) {
        return new Comprobante("CFDI (SAT)", "CFDI-MX-" + idPedido.replaceFirst("^PED-", ""),
                base, nombreImpuesto, impuesto, base.add(impuesto));
    }
}

