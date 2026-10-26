package co.edu.unbosque.shopflow.pagos;

import java.util.List;
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
}
