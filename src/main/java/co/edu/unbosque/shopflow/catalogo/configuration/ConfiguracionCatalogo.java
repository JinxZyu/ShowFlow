package co.edu.unbosque.shopflow.catalogo.configuration;


import co.edu.unbosque.shopflow.catalogo.CatalogoRemoto;
import co.edu.unbosque.shopflow.catalogo.proxy.CatalogoCacheProxy;
import org.springframework.context.annotation.*;

@Configuration
public class ConfiguracionCatalogo {

    @Bean
    public co.edu.unbosque.shopflow.catalogo.domein.ServicioCatalogo servicioCatalogo() {
        return new CatalogoCacheProxy(new CatalogoRemoto());
    }
}