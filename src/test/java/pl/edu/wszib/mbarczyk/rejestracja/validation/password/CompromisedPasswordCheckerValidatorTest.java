package pl.edu.wszib.mbarczyk.rejestracja.validation.password;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;


class CompromisedPasswordCheckerValidatorTest {

    CompromisedPasswordCheckerValidator validator;

    @BeforeEach
    void setUp() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.pwnedpasswords.com")
                .defaultHeader("User-Agent", "WSZiB Test PasswordCheck")
                .build();

        validator = new CompromisedPasswordCheckerValidator(restClient);
    }

    @Test
    void shouldDetectCompromisedPassword() {
        //given
        String password = "dupa123";
        //when
        //then
        Assertions.assertThatThrownBy(()->validator.accept(password))
                .isInstanceOf(EmailValidationException.class)
                .hasMessageContaining("wyciekło");
    }

    @Test
    void shouldAcceptNotCompromisedPassword() {
        //given
        String password = RandomStringUtils.insecure().next(15, true, true);
        //when
        //then
        Assertions.assertThatNoException()
                .isThrownBy(()->validator.accept(password));
    }


}