package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayTimeoutException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class AuditoriaController {

    private final RegistroAuditoria registro;

    public AuditoriaController(RegistroAuditoria registro) {
        this.registro = registro;
    }

    @GetMapping("/auditoria")
    public List<RegistroAuditoria.EntradaAuditoria> listar() {
        return registro.todas();
    }

    public static class LegacyPayAdapter implements PasarelaDePago {

        private final LegacyPayClient legacyPayClient;

        public LegacyPayAdapter(LegacyPayClient legacyPayClient){
            this.legacyPayClient = legacyPayClient;
        }

        @Override
        public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
            long centavos = montoUsd.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
            String respuesta;
            try {
                respuesta = legacyPayClient.makeCharge(centavos, "USD", referencia);
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
}
