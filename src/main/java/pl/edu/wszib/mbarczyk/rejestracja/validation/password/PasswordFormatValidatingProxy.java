package pl.edu.wszib.mbarczyk.rejestracja.validation.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.validation.PasswordValidationException;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class PasswordFormatValidatingProxy implements Consumer<String> {

    private static final Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,32}$");

    private final Consumer<String> proxiedValidator;

    @Override
    public void accept(String password) {

        if (pattern.matcher(password).matches()) {
            log.info("Password meets syntax complexity requirements. Checking if compromised");
            proxiedValidator.accept(password);
        } else {
            log.info("Password too simple: {}", password);
            throw new PasswordValidationException("Hasło nie spełnia wymagań złożoności");
        }
    }
}
