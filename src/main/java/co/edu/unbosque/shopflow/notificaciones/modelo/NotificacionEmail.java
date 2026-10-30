package co.edu.unbosque.shopflow.notificaciones.modelo;

import co.edu.unbosque.shopflow.notificaciones.infraestructura.BandejaNotificaciones;

public class NotificacionEmail implements Notificacion {
	

	private final BandejaNotificaciones bandejaNotificaciones;
	
	public NotificacionEmail(BandejaNotificaciones bandejaNotificaciones) {
		this.bandejaNotificaciones = bandejaNotificaciones;
	}

	@Override
	public void enviar(String destino, String mensaje) {
		if(destino == null || !destino.contains("@")) {
			throw new IllegalArgumentException (
				"El correo debe contener @"
			);
		}
		bandejaNotificaciones.registrar("EMAIL", destino, mensaje);
			
		
	}

}
