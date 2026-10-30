package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

/** Comprobante electrónico emitido para un pedido (formato propio de cada país). */
public record Comprobante(String tipo, String numero, BigDecimal base,
                          String nombreImpuesto, BigDecimal impuesto, BigDecimal total) { }
