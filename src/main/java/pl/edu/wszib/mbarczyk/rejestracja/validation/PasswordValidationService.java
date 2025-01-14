package pl.edu.wszib.mbarczyk.rejestracja.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class PasswordValidationService extends AbstractValidationService{

    private final Consumer<String> passwordValidatingConsumer;

    @Override
    protected void validateUser(User user) {
        try {
            passwordValidatingConsumer.accept(Optional.ofNullable(user.password())
                    .orElseThrow(()->new PasswordValidationException("Hasło nie może być puste!"))
            );
        } catch (PasswordValidationException eve) {
            ValidationException.throwException(user, "Hasło nie spełnia wymagań bezpieczeństwa: " + eve.getMessage());
        }
    }
}
