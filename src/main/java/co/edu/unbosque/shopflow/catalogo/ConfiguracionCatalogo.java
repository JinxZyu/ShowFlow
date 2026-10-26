package co.edu.unbosque.shopflow.catalogo;

import org.springframework.context.annotation.*;

@Configuration
public class ConfiguracionCatalogo {

    @Bean
    public ServicioCatalogo servicioCatalogo() {
        return new CatalogoRemoto();
        // cuando exista el proxy: return new CatalogoCacheProxy(new CatalogoRemoto());
    }
}