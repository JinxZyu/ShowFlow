package co.edu.unbosque.shopflow.notificaciones;

import java.math.BigDecimal;

public abstract class Notificador {

    public abstract String canal();

    public final void notificarPagoAprobado(String destino, String idPedido, BigDecimal total) {
        String mensaje = "Tu pago del pedido " + idPedido + " por " + total + " USD fue aprobado. "
                + "Gracias por comprar en ShopFlow.";
        Notificacion notificacion = crearNotificacion();   // ← método fábrica
        notificacion.enviar(destino, mensaje);
    }

    protected abstract Notificacion crearNotificacion();
}
