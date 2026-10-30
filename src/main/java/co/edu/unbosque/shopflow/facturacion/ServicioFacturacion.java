package co.edu.unbosque.shopflow.facturacion;

import co.edu.unbosque.shopflow.pedidos.Pedido;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class ServicioFacturacion {

    private final Map<String, FabricaFacturacion> fabricas;

    public ServicioFacturacion(List<FabricaFacturacion> fabricas) {
        this.fabricas = fabricas.stream()
                .collect(Collectors.toUnmodifiableMap(FabricaFacturacion::pais, Function.identity()));
    }

    public Comprobante facturar(Pedido pedido) {
        FabricaFacturacion fabrica = Optional.ofNullable(fabricas.get(pedido.getPais()))
                .orElseThrow(() -> new IllegalArgumentException("País no soportado: " + pedido.getPais()));

        fabrica.crearValidador().validar(pedido.getDocumentoCliente());

        CalculadoraImpuestos calculadora = fabrica.crearCalculadora();
        BigDecimal base = pedido.getSubtotal().subtract(pedido.getDescuento());
        BigDecimal impuesto = calculadora.calcular(base);

        return fabrica.crearGenerador().generar(pedido.getId(), base, calculadora.nombreImpuesto(), impuesto);
    }
}
