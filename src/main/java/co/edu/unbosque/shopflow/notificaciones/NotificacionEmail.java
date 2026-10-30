package co.edu.unbosque.shopflow.notificaciones;

public class NotificacionEmail implements Notificacion {

    private static final String ASUNTO = "ShopFlow: pago aprobado";

    private final BandejaNotificaciones bandeja;

    public NotificacionEmail(BandejaNotificaciones bandeja) {
        this.bandeja = bandeja;
    }

    @Override
    public void enviar(String destino, String mensaje) {
        if (destino == null || !destino.contains("@")) {
            throw new IllegalArgumentException("Para notificar por correo el contacto debe tener «@»: " + destino);
        }
        bandeja.registrar("EMAIL", destino, "[" + ASUNTO + "] " + mensaje);
    }
}
