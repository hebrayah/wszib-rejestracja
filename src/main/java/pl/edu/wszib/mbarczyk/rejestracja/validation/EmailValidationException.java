package pl.edu.wszib.mbarczyk.rejestracja.validation;

public class EmailValidationException extends RuntimeException{
    public EmailValidationException() {
    }

    public EmailValidationException(String message) {
        super(message);
    }

    public EmailValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
