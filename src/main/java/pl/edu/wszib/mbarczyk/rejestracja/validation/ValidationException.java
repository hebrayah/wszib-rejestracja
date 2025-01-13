package pl.edu.wszib.mbarczyk.rejestracja.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

public class ValidationException extends ResponseStatusException {
    private User user;
    private String errorMessage;

    public ValidationException(User user, String errorMessage) {
        super(HttpStatus.BAD_REQUEST,
                String.format("Dane Uzytkownika: %s zawierają błędy. %s", StringUtils.defaultIfEmpty(user.username(), "Unknown"), errorMessage),
                null,
                (String) null,
                (Object[]) null
        );
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public static void throwException(User user, String message) {
        throw new ValidationException(user, message);
    }

    public User getUser() {
        return this.user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
