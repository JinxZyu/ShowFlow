package co.edu.unbosque.shopflow.facturacion;

import java.util.Locale;
import java.util.regex.Pattern;

public class ValidadorRfc implements ValidadorIdentificacion {

    private static final Pattern FORMATO = Pattern.compile("[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}");

    @Override
    public void validar(String documento) {
        if (documento == null || !FORMATO.matcher(documento.trim().toUpperCase(Locale.ROOT)).matches()) {
            throw new IllegalArgumentException("Para México el documento debe ser un RFC válido");
        }
    }
}

