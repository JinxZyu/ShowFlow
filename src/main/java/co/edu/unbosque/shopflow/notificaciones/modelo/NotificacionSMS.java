package co.edu.unbosque.shopflow.notificaciones.modelo;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones;
public class NotificacionSMS implements Notificacion {
	
	private BandejaNotificaciones bandejaNotificaciones;
	
	public NotificacionSMS(BandejaNotificaciones bandejaNotificaciones) {
		this.bandejaNotificaciones = bandejaNotificaciones;
	}

	@Override
	public void enviar(String destino, String mensaje) {
		String mensajeCorto = mensaje.length() > 160 ? mensaje.substring(160): mensaje;
		bandejaNotificaciones.registrar("SMS", destino, mensajeCorto);
	}

}
