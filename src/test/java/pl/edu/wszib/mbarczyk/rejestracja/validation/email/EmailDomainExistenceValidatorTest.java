package pl.edu.wszib.mbarczyk.rejestracja.validation.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;

class EmailDomainExistenceValidatorTest {

    EmailDomainExistenceValidator validator = new EmailDomainExistenceValidator("dns://8.8.8.8");

    @Test
    void shouldPassOnCorrectAddress() {
        //given
        String email = "something@gmail.com";
        //when
        //then
        Assertions.assertThatNoException()
                .isThrownBy(()-> validator.accept(email));
    }

    @Test
    void shouldFailOnNonExistentDomain() {
        //given
        String email = "something@zzz.dfg";
        //when
        //then
        Assertions.assertThatThrownBy(()-> validator.accept(email)).isInstanceOf(EmailValidationException.class);
    }
}