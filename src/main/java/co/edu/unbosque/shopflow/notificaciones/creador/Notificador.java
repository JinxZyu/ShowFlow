package co.edu.unbosque.shopflow.notificaciones.creador;

import java.math.BigDecimal;

import co.edu.unbosque.shopflow.notificaciones.modelo.Notificacion;

public abstract class Notificador {

	public final void notificarPagoAprobado(String destino, String pedidoId, BigDecimal total) {
		String mensaje = ("Pago aprobado para el pedido "+pedidoId+".\nTotal pagado: "+total+"USD.");
		Notificacion notificacion = crearNotificacion();
		notificacion.enviar(destino, mensaje);
	}
	
	protected abstract Notificacion crearNotificacion();
	public abstract String canal();
}
