package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPaySdk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ConfiguracionPagos {
    @Bean
    public Map<String, PasarelaDePago> pasarelas(
            @Value("${shopflow.legacypay.probabilidad-timeout}") double probabilidadTimeout) {

        PasarelaDePago legacyPay = new AuditoriaController.LegacyPayAdapter(new LegacyPayClient(probabilidadTimeout));
        PasarelaDePago quickPay = new QuickPayAdapter(new QuickPaySdk());

        return Map.of(
                "LEGACYPAY", legacyPay,
                "QUICKPAY", quickPay);
    }
}
