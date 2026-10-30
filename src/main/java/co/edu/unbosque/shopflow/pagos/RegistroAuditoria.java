package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class RegistroAuditoria {

    public record EntradaAuditoria(String proveedor, BigDecimal monto, boolean aprobado,
                                   String mensaje, long duracionMs, LocalDateTime fecha) { }

    private final List<EntradaAuditoria> entradas = new CopyOnWriteArrayList<>();

    public void registrar(String proveedor, BigDecimal monto, boolean aprobado,
                          String mensaje, long duracionMs) {
        entradas.add(new EntradaAuditoria(proveedor, monto, aprobado, mensaje, duracionMs, LocalDateTime.now()));
    }

    public List<EntradaAuditoria> todas() {

        return List.copyOf(entradas);
    }
}
