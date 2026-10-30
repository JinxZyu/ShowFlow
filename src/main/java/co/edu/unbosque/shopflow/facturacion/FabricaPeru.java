package co.edu.unbosque.shopflow.facturacion;

import org.springframework.stereotype.Component;

@Component
public class FabricaPeru implements FabricaFacturacion {

    @Override
    public String pais() {
        return "PE";
    }

    @Override
    public CalculadoraImpuestos crearCalculadora() {
        return new CalculadoraIgvPeru();
    }

    @Override
    public ValidadorIdentificacion crearValidador() {
        return new ValidadorRucPeru();
    }

    @Override
    public GeneradorComprobante crearGenerador() {
        return new GeneradorFacturaSunat();
    }
}