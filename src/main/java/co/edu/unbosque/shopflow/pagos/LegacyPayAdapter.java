package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayTimeoutException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Adapter: traduce PasarelaDePago (lo que ShopFlow espera) a LegacyPayClient (el SDK ajeno). */
public class LegacyPayAdapter implements PasarelaDePago {

    private final LegacyPayClient cliente;

    public LegacyPayAdapter(LegacyPayClient cliente) {
        this.cliente = cliente;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        long centavos = montoUsd.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
        String respuesta;
        try {
            respuesta = cliente.makeCharge(centavos, "USD", referencia);
        } catch (LegacyPayTimeoutException e) {
            throw new PasarelaNoDisponibleException("LegacyPay no responde", e);
        }
        String[] partes = respuesta.split("\\|", -1);
        String codigo = partes[0];
        String autorizacion = partes.length > 1 ? partes[1] : "";

        return switch (codigo) {
            case "00" -> ResultadoPago.aprobado(autorizacion);
            case "51" -> ResultadoPago.rechazado("Fondos insuficientes");
            default   -> ResultadoPago.rechazado("Transacción no permitida");
        };
    }
}