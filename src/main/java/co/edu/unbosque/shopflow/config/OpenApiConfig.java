package co.edu.unbosque.shopflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shopflowOpenApi() {
        return new OpenAPI().info(new Info()
                .title("ShopFlow - API de pedidos")
                .version("1.0.0")
                .description("API REST de pedidos de ShopFlow: catálogo, pedidos, facturación por país, "
                        + "pagos con LegacyPay y QuickPay, auditoría y notificaciones. "
                        + "Taller de patrones de diseño creacionales y estructurales - Ingeniería de Software 2."));
    }
}