package co.edu.unbosque.shopflow.facturacion;

import java.util.regex.Pattern;

public class ValidadorCedulaNit implements ValidadorIdentificacion {

    private static final Pattern FORMATO = Pattern.compile("\\d{6,10}");

    @Override
    public void validar(String documento) {
        if (documento == null || !FORMATO.matcher(documento.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Para Colombia el documento debe ser una cédula o NIT de 6 a 10 dígitos");
        }
    }
}

