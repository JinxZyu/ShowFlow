package co.edu.unbosque.shopflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//Swagger
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI electroshopOpenApi() {
        return new OpenAPI().info(new Info()
                .title("ElectroShop - API de pedidos")
                .version("1.0.0")
                .description("API para procesar pedidos (validación, reserva de inventario, "
                        + "pago simulado y orden de preparación) y consultar su detalle. "
                        + "Taller de rendimiento con JMeter - Ingeniería de Software II."));
    }
}