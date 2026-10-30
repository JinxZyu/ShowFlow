package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.quickpay.QuickPayRequest;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPayResult;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPaySdk;

import java.math.BigDecimal;

public class QuickPayAdapter implements PasarelaDePago {

    private final QuickPaySdk sdk;

    public QuickPayAdapter (QuickPaySdk sdk){
        this.sdk=sdk;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        QuickPayRequest peticion = new QuickPayRequest(referencia, montoUsd.doubleValue(), "USD");
        QuickPayResult resultado = sdk.pay(peticion);
        return switch (resultado.status()) {
            case OK       -> ResultadoPago.aprobado(resultado.transactionId());
            case REJECTED -> ResultadoPago.rechazado("Rechazado por QuickPay: " + resultado.detail());
            case ERROR    -> ResultadoPago.rechazado("Error en QuickPay: " + resultado.detail());
        };
    }
}
