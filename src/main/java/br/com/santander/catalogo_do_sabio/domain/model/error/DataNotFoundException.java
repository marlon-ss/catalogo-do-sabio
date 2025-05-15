package br.com.santander.catalogo_do_sabio.domain.model.error;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
