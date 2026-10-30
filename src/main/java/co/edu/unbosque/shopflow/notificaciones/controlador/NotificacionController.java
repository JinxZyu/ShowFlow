
package co.edu.unbosque.shopflow.notificaciones.controlador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.shopflow.notificaciones.creador.Notificador;
import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones.NotificacionEnviada;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private BandejaNotificaciones bandeja;

    @Autowired
    private List<Notificador> notificadores;

    @GetMapping
    public List<NotificacionEnviada> listar() {
        return bandeja.todas();
    }

    @PostMapping("/prueba")
    public BandejaNotificaciones.NotificacionEnviada probar(
            @RequestBody SolicitudPrueba solicitud) {

        String canal = solicitud.canal() == null
                ? ""
                : solicitud.canal().trim().toUpperCase(Locale.ROOT);

        Notificador notificador = notificadores.stream()
                .filter(n -> n.canal().equalsIgnoreCase(canal))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Canal no soportado: " + canal
                    )
                );

        notificador.notificarPagoAprobado(
                solicitud.destino(),
                solicitud.pedidoId(),
                solicitud.total()
        );

        List<NotificacionEnviada> enviadas =
                bandeja.todas();

        return enviadas.get(enviadas.size() - 1);
    }

    public record SolicitudPrueba(
            String canal,
            String destino,
            String pedidoId,
            BigDecimal total) {
    }
}
