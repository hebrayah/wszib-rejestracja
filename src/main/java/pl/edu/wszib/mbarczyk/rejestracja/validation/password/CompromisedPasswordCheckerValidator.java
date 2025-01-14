package pl.edu.wszib.mbarczyk.rejestracja.validation.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;
import pl.edu.wszib.mbarczyk.rejestracja.validation.PasswordValidationException;

import java.util.Arrays;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class CompromisedPasswordCheckerValidator implements Consumer<String> {

    private final RestClient verificationRestClient;

    @Override
    public void accept(String password) {
        log.debug("Checking whether password has been compromised");
        String sh1 = DigestUtils.sha1Hex(password);
        log.info("Hash: {}", sh1);
        String key = sh1.substring(0, 5);
        String matching = sh1.substring(5, sh1.length());
        log.info("Part of the SHA1 key: {}", key);
        log.info("Matching: {}", matching);

        String body = verificationRestClient.get()
                .uri("/range/{key}", key)
                .retrieve()
                .onStatus(status->status.value()== HttpStatus.NOT_FOUND.value(), (request, response) ->{
                    throw new PasswordValidationException("Nie można sprawdzić hasła");
                })
                .body(String.class);
        log.info("Received body: {}", StringUtils.left(body, 200));
        boolean result = Arrays.stream(body.split("\n"))
                .map(String::trim)
                .map(String::toLowerCase)
                .anyMatch(suffix -> suffix.startsWith(matching));
        if (result) {
            throw new EmailValidationException("To hasło wyciekło i nie można go użyć!");
        }
    }
}
