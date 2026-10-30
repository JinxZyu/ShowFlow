package co.edu.unbosque.shopflow.notificaciones.creador;

import org.springframework.stereotype.Component;

import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.modelo.Notificacion;
import co.edu.unbosque.shopflow.notificaciones.modelo.NotificacionEmail;

@Component
public class NotificadorEmail extends Notificador{

	
	private final BandejaNotificaciones bandejaNotificaciones;
	
	public NotificadorEmail(BandejaNotificaciones bandejaNotificaciones) {
		this.bandejaNotificaciones = bandejaNotificaciones;
	}
	
	@Override
	protected Notificacion crearNotificacion() {
		return new NotificacionEmail(bandejaNotificaciones);
	}

	@Override
	public String canal() {
		return "EMAIL";
	}

	
}
