package pl.edu.wszib.mbarczyk.rejestracja.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class EmailValidationService extends AbstractValidationService {

    private final Consumer<String> emailValidatingConsumer;

    @Override
    protected void validateUser(User user) {
        log.info("Validating email for username: {} [{}]",user.username(), user.email());
        if(StringUtils.isBlank( user.username())) {
            ValidationException.throwException(user, "Nazwa użytkownika nie może być pusta.");
        }

        try {
            emailValidatingConsumer.accept(user.email());
        } catch (EmailValidationException eve) {
            ValidationException.throwException(user, "Adres email zawiera błędy: " + eve.getMessage());
        }
    }
}
