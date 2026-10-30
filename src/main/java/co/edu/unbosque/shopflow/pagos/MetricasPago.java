package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricasPago extends PasarelaDecorator {

    private final String proveedor;
    private final AtomicInteger cobros = new AtomicInteger();
    private final AtomicInteger aprobados = new AtomicInteger();

    public MetricasPago(PasarelaDePago envuelta, String proveedor) {
        super(envuelta);
        this.proveedor = proveedor;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        try {
            ResultadoPago resultado = super.cobrar(montoUsd, referencia);
            cobros.incrementAndGet();
            if (resultado.aprobado()) {
                aprobados.incrementAndGet();
            }
            return resultado;
        } catch (PasarelaNoDisponibleException e) {
            cobros.incrementAndGet();
            throw e;
        } finally {
            System.out.printf("[Metricas] %s: %d/%d aprobados (%.1f %%)%n",
                    proveedor, aprobados.get(), cobros.get(), tasaAprobacion());
        }
    }

    public int cobros() {
        return cobros.get();
    }

    public int aprobados() {
        return aprobados.get();
    }
    public double tasaAprobacion() {
        int total = cobros.get();
        return total == 0 ? 0.0 : 100.0 * aprobados.get() / total;
    }
}
