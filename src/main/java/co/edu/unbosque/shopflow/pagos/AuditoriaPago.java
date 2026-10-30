package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;

public class AuditoriaPago extends PasarelaDecorator {

    private final String proveedor;
    private final RegistroAuditoria registro;

    public AuditoriaPago(PasarelaDePago envuelta, String proveedor, RegistroAuditoria registro) {
        super(envuelta);
        this.proveedor = proveedor;
        this.registro = registro;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        long inicio = System.currentTimeMillis();
        try {
            ResultadoPago resultado = super.cobrar(montoUsd, referencia);   // delega en la envuelta
            registro.registrar(proveedor, montoUsd, resultado.aprobado(), resultado.mensaje(),
                    System.currentTimeMillis() - inicio);
            return resultado;
        } catch (PasarelaNoDisponibleException e) {
            registro.registrar(proveedor, montoUsd, false, "Pasarela no disponible",
                    System.currentTimeMillis() - inicio);
            throw e;
        }
    }
}