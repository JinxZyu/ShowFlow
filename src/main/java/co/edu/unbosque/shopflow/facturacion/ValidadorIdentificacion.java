package co.edu.unbosque.shopflow.facturacion;

/** AbstractProduct B: valida el documento del cliente según el país. */
public interface ValidadorIdentificacion {
    /** @throws IllegalArgumentException si el documento no cumple el formato del país */
    void validar(String documento);
}
