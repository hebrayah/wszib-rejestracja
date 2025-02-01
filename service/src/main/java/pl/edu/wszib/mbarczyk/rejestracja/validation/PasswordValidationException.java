package pl.edu.wszib.mbarczyk.rejestracja.validation;

public class PasswordValidationException extends RuntimeException{
    public PasswordValidationException() {
    }

    public PasswordValidationException(String message) {
        super(message);
    }

    public PasswordValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
