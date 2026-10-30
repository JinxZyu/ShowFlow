package co.edu.unbosque.shopflow.notificaciones.creador;

import org.springframework.stereotype.Component;

import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.modelo.Notificacion;
import co.edu.unbosque.shopflow.notificaciones.modelo.NotificacionSMS;

@Component
public class NotificadorSMS extends Notificador {

	private final BandejaNotificaciones bandejaNotificaciones;
	
	public NotificadorSMS(BandejaNotificaciones bandejaNotificaciones) {
		this.bandejaNotificaciones = bandejaNotificaciones;
	}
	@Override
	protected Notificacion crearNotificacion() {
		// TODO Auto-generated method stub
		return new NotificacionSMS(bandejaNotificaciones);
	}

	@Override
	public String canal() {
		return "SMS";
	}

	
}
