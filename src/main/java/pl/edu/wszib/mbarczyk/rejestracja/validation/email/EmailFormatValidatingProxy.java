package pl.edu.wszib.mbarczyk.rejestracja.validation.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class EmailFormatValidatingProxy implements Consumer<String> {

    private final Consumer<String> domainEmailValidator;

    public static final Pattern emailFormatPattern = Pattern.compile(  "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @Override
    public void accept(String email) {
        if (emailFormatPattern.matcher(email).matches()){
            log.info("Email syntax is correct: {}, checking existence", email);
            domainEmailValidator.accept(email);
        } else {
            throw new EmailValidationException("Format adresu email jest niepoprawny.");
        }
    }
}
