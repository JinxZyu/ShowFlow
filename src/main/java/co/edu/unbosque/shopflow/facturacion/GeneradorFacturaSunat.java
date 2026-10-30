package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

public class GeneradorFacturaSunat implements GeneradorComprobante {

    @Override
    public Comprobante generar(String idPedido, BigDecimal base,
                               String nombreImpuesto, BigDecimal impuesto) {

        return new Comprobante(
                "Factura electrónica (SUNAT)",
                "FE-PE-" + idPedido.replaceFirst("^PED-", ""),
                base,
                nombreImpuesto,
                impuesto,
                base.add(impuesto)
        );
    }
}
