package co.edu.unbosque.shopflow.facturacion;

import org.springframework.stereotype.Component;

/** ConcreteFactory: familia de facturación de México. */
@Component
public class FabricaMexico implements FabricaFacturacion {

    @Override
    public String pais() {
        return "MX";
    }

    @Override
    public CalculadoraImpuestos crearCalculadora() {
        return new CalculadoraIvaMexico();
    }

    @Override
    public ValidadorIdentificacion crearValidador() {
        return new ValidadorRfc();
    }

    @Override
    public GeneradorComprobante crearGenerador() {
        return new GeneradorCfdiSat();
    }
}
