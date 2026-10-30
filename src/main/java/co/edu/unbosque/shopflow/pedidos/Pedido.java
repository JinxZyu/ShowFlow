package co.edu.unbosque.shopflow.pedidos;

<<<<<<< Updated upstream
/** PROVISIONAL: lo reemplaza quien haga el Builder. */
=======
import co.edu.unbosque.shopflow.facturacion.Comprobante;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


>>>>>>> Stashed changes
public final class Pedido {

    public static final String CUPON_BOSQUE10 = "BOSQUE10";
    private static final BigDecimal PORCENTAJE_BOSQUE10 = new BigDecimal("0.10");
    private static final int MAX_NOTA_REGALO = 140;

    public enum Estado { CREADO, PAGADO }

    private final String id;
    private final Estado estado;
    private final String clienteId;
    private final String documentoCliente;
    private final String pais;
    private final String contacto;
    private final List<ItemPedido> items;
    private final String direccionEnvio;
    private final boolean recogerEnTienda;
    private final String cupon;
    private final String notaRegalo;
    private final boolean envioExpress;
    private final String canalNotificacion;
    private final BigDecimal subtotal;
    private final BigDecimal descuento;
    private final Comprobante comprobante;
    private final String autorizacionPago;

    private Pedido(Builder b, BigDecimal subtotal, BigDecimal descuento) {
        this.id = b.id != null ? b.id : "PED-" + UUID.randomUUID().toString().substring(0, 8);
        this.estado = Estado.CREADO;
        this.clienteId = b.clienteId;
        this.documentoCliente = b.documentoCliente;
        this.pais = b.pais;
        this.contacto = b.contacto;
        this.items = List.copyOf(b.items);
        this.direccionEnvio = b.direccionEnvio;
        this.recogerEnTienda = b.recogerEnTienda;
        this.cupon = b.cupon;
        this.notaRegalo = b.notaRegalo;
        this.envioExpress = b.envioExpress;
        this.canalNotificacion = b.canalNotificacion;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.comprobante = null;
        this.autorizacionPago = null;
    }

    /** Constructor de copia para los cambios de estado. */
    private Pedido(Pedido o, Estado estado, Comprobante comprobante, String autorizacionPago) {
        this.id = o.id;
        this.estado = estado;
        this.clienteId = o.clienteId;
        this.documentoCliente = o.documentoCliente;
        this.pais = o.pais;
        this.contacto = o.contacto;
        this.items = o.items;
        this.direccionEnvio = o.direccionEnvio;
        this.recogerEnTienda = o.recogerEnTienda;
        this.cupon = o.cupon;
        this.notaRegalo = o.notaRegalo;
        this.envioExpress = o.envioExpress;
        this.canalNotificacion = o.canalNotificacion;
        this.subtotal = o.subtotal;
        this.descuento = o.descuento;
        this.comprobante = comprobante;
        this.autorizacionPago = autorizacionPago;
    }

    public Pedido conComprobante(Comprobante nuevo) {
        return new Pedido(this, estado, nuevo, autorizacionPago);
    }

    public Pedido conPagoAprobado(String autorizacion) {
        return new Pedido(this, Estado.PAGADO, comprobante, autorizacion);
    }

    public String getId() { return id; }
    public Estado getEstado() { return estado; }
    public String getClienteId() { return clienteId; }
    public String getDocumentoCliente() { return documentoCliente; }
    public String getPais() { return pais; }
    public String getContacto() { return contacto; }
    public List<ItemPedido> getItems() { return items; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public boolean isRecogerEnTienda() { return recogerEnTienda; }
    public String getCupon() { return cupon; }
    public String getNotaRegalo() { return notaRegalo; }
    public boolean isEnvioExpress() { return envioExpress; }
    public String getCanalNotificacion() { return canalNotificacion; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public Comprobante getComprobante() { return comprobante; }
    public String getAutorizacionPago() { return autorizacionPago; }

    /** Builder de Bloch: obligatorios en el constructor, opciones encadenadas, validación en build(). */
    public static final class Builder {

        // obligatorios
        private final String clienteId;
        private final String documentoCliente;
        private final String pais;
        private final String contacto;
        private final List<ItemPedido> items = new ArrayList<>();

        // opcionales
        private String id;
        private String direccionEnvio;
        private boolean recogerEnTienda;
        private String cupon;
        private String notaRegalo;
        private boolean envioExpress;
        private String canalNotificacion;

        public Builder(String clienteId, String documentoCliente, String pais, String contacto) {
            this.clienteId = obligatorio(clienteId, "El cliente es obligatorio");
            this.documentoCliente = obligatorio(documentoCliente, "El documento del cliente es obligatorio");
            this.pais = obligatorio(pais, "El país es obligatorio").toUpperCase(Locale.ROOT);
            this.contacto = obligatorio(contacto, "El contacto es obligatorio");
        }

        public Builder item(ItemPedido item) {
            this.items.add(item);
            return this;
        }

        public Builder id(String id) {
            this.id = vacioANulo(id);
            return this;
        }

        public Builder direccionEnvio(String direccionEnvio) {
            this.direccionEnvio = vacioANulo(direccionEnvio);
            return this;
        }

        public Builder recogerEnTienda(boolean recogerEnTienda) {
            this.recogerEnTienda = recogerEnTienda;
            return this;
        }

        public Builder cupon(String cupon) {
            this.cupon = vacioANulo(cupon);
            return this;
        }

        public Builder notaRegalo(String notaRegalo) {
            this.notaRegalo = vacioANulo(notaRegalo);
            return this;
        }

        public Builder envioExpress(boolean envioExpress) {
            this.envioExpress = envioExpress;
            return this;
        }

        public Builder canalNotificacion(String canalNotificacion) {
            this.canalNotificacion = vacioANulo(canalNotificacion) == null
                    ? null : canalNotificacion.trim().toUpperCase(Locale.ROOT);
            return this;
        }

        /** Único punto donde se validan las reglas que cruzan varias opciones. */
        public Pedido build() {
            if (items.isEmpty()) {
                throw new IllegalArgumentException("El pedido debe tener al menos un ítem");
            }
            if (!recogerEnTienda && direccionEnvio == null) {
                throw new IllegalArgumentException(
                        "La dirección de envío es obligatoria cuando el pedido no se recoge en tienda");
            }
            if (recogerEnTienda && envioExpress) {
                throw new IllegalArgumentException(
                        "Un pedido para recoger en tienda no puede ser de envío express");
            }
            if (notaRegalo != null && notaRegalo.length() > MAX_NOTA_REGALO) {
                throw new IllegalArgumentException(
                        "La nota de regalo admite máximo " + MAX_NOTA_REGALO + " caracteres");
            }
            BigDecimal subtotal = items.stream()
                    .map(ItemPedido::subtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
            return new Pedido(this, subtotal, calcularDescuento(subtotal));
        }

        private BigDecimal calcularDescuento(BigDecimal subtotal) {
            if (cupon == null) {
                return BigDecimal.ZERO.setScale(2);
            }
            if (!CUPON_BOSQUE10.equalsIgnoreCase(cupon)) {
                throw new IllegalArgumentException("El cupón " + cupon + " no es válido");
            }
            return subtotal.multiply(PORCENTAJE_BOSQUE10).setScale(2, RoundingMode.HALF_UP);
        }

        private static String obligatorio(String valor, String mensaje) {
            if (valor == null || valor.isBlank()) {
                throw new IllegalArgumentException(mensaje);
            }
            return valor.trim();
        }

        private static String vacioANulo(String valor) {
            return valor == null || valor.isBlank() ? null : valor.trim();
        }
    }
}
