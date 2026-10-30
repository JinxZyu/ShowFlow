package co.edu.unbosque.shopflow.facturacion;

import java.util.regex.Pattern;

public class ValidadorRucPeru implements ValidadorIdentificacion {

    private static final Pattern FORMATO = Pattern.compile("\\d{11}");

    @Override
    public void validar(String documento) {
        if (documento == null || !FORMATO.matcher(documento.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Para Perú el documento debe ser un RUC de 11 dígitos");
        }
    }
}