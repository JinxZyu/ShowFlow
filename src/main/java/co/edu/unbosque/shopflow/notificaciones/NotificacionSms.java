package co.edu.unbosque.shopflow.notificaciones;

public class NotificacionSms implements Notificacion {

    static final int MAX_CARACTERES = 160;

    private final BandejaNotificaciones bandeja;

    public NotificacionSms(BandejaNotificaciones bandeja) {
        this.bandeja = bandeja;
    }

    @Override
    public void enviar(String destino, String mensaje) {
        String texto = mensaje.length() > MAX_CARACTERES ? mensaje.substring(0, MAX_CARACTERES) : mensaje;
        bandeja.registrar("SMS", destino, texto);
    }
}
