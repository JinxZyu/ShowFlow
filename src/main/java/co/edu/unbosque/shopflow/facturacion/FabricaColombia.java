package co.edu.unbosque.shopflow.facturacion;

import org.springframework.stereotype.Component;

/** ConcreteFactory: familia de facturación de Colombia. */
@Component
public class FabricaColombia implements FabricaFacturacion {

    @Override
    public String pais() {
        return "CO";
    }

    @Override
    public CalculadoraImpuestos crearCalculadora() {
        return new CalculadoraIvaColombia();
    }

    @Override
    public ValidadorIdentificacion crearValidador() {
        return new ValidadorCedulaNit();
    }

    @Override
    public GeneradorComprobante crearGenerador() {
        return new GeneradorFacturaDian();
    }
}