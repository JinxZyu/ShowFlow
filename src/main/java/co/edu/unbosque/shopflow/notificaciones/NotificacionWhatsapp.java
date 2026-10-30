package co.edu.unbosque.shopflow.notificaciones;

//Notificación de whastaap
public class NotificacionWhatsapp implements Notificacion {

    private final BandejaNotificaciones bandeja;

    public NotificacionWhatsapp(BandejaNotificaciones bandeja) {
        this.bandeja = bandeja;
    }

    @Override
    public void enviar(String destino, String mensaje) {
        if (destino == null || !destino.matches("^\\+\\d{10,15}$")) {
            throw new IllegalArgumentException("El número de WhatsApp debe incluir el indicativo (por ejemplo, +573001234567)");
        }
        bandeja.registrar("WHATSAPP", destino, "✅ " + mensaje);
    }
}