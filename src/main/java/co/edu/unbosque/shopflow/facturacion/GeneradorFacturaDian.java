package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

/** ConcreteProduct C (Colombia): factura electrónica de la DIAN. */
public class GeneradorFacturaDian implements GeneradorComprobante {

    @Override
    public Comprobante generar(String idPedido, BigDecimal base, String nombreImpuesto, BigDecimal impuesto) {
        return new Comprobante("Factura electrónica (DIAN)", "FE-CO-" + idPedido.replaceFirst("^PED-", ""),
                base, nombreImpuesto, impuesto, base.add(impuesto));
    }
}
