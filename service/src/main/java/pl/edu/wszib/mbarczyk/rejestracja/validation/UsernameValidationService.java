package pl.edu.wszib.mbarczyk.rejestracja.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.util.regex.Pattern;

@Slf4j
public class UsernameValidationService extends AbstractValidationService {

    private static final String usernamePattern = "^[a-zA-Z0-9.]{3,10}$";

    @Override
    protected void validateUser(User user) {
        log.info("Validating username: {}",user.username());
        if(StringUtils.isBlank( user.username())) {
            ValidationException.throwException(user, "Nazwa użytkownika nie może być pusta.");
        }

        if (!Pattern.matches(usernamePattern, user.username())) {
            ValidationException.throwException(user, "Nazwa użytkownika musi się składać z 3 do 10 znaków. Dopuszczalne są cyfry, litery oraz znak \".\".");
        }
    }
}


