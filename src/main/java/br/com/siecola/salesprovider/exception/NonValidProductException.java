package br.com.siecola.salesprovider.exception;

public class NonValidProductException extends Exception {
    private String message;

    public NonValidProductException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}