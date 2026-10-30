package co.edu.unbosque.shopflow.facturacion;

/** AbstractFactory: entrega la familia completa de piezas de facturación de un país. */
public interface FabricaFacturacion {
    String pais();
    CalculadoraImpuestos crearCalculadora();
    ValidadorIdentificacion crearValidador();
    GeneradorComprobante crearGenerador();
}
