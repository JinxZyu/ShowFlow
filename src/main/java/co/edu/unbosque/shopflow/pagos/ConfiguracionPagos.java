package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPaySdk;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionPagos {

    @Value("${shopflow.legacypay.probabilidad-timeout}")
    private double probabilidadTimeout;

    @Value("${shopflow.pagos.reintentos}")
    private boolean reintentos;

    @Value("${shopflow.pagos.max-intentos}")
    private int maxIntentos;

    @Value("${shopflow.pagos.auditoria}")
    private boolean auditoria;

    @Bean
    public Map<String, PasarelaDePago> pasarelas(RegistroAuditoria registro) {
        PasarelaDePago legacyPay = new LegacyPayAdapter(new LegacyPayClient(probabilidadTimeout));
        PasarelaDePago quickPay = new QuickPayAdapter(new QuickPaySdk());

        return Map.of(
                "LEGACYPAY", armarCapas("LEGACYPAY", legacyPay, registro),
                "QUICKPAY", armarCapas("QUICKPAY", quickPay, registro));
    }

    private PasarelaDePago armarCapas(String proveedor, PasarelaDePago adapter, RegistroAuditoria registro) {
        PasarelaDePago pasarela = adapter;
        if (reintentos) {
            pasarela = new ReintentoPago(pasarela, maxIntentos);
        }
        if (auditoria) {
            pasarela = new AuditoriaPago(pasarela, proveedor, registro);
        }
        pasarela = new MetricasPago(pasarela, proveedor);
        return pasarela;
    }
}
