package pl.edu.wszib.mbarczyk.rejestracja.validation;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PasswordValidationServiceTest {

    @Mock
    Consumer<String> validationConsumer;

    @InjectMocks
    PasswordValidationService service;

    @Test
    void shouldFailOnNullPassword() {
        //given
        User user = new User("aaa", "aaa@ddf.com", null);
        //when/then
        Assertions.assertThatThrownBy(()->service.validateUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("puste");
        Mockito.verifyNoInteractions(validationConsumer);
    }

    @Test
    void shouldCallValidatorAndReThrowException() {
        //given
        String password = RandomStringUtils.insecure().nextAlphabetic(10);
        User user = new User("aaa", "aaa@ddf.com", password);
        PasswordValidationException pe = new PasswordValidationException("some validation issue");
        Mockito.doThrow(pe).when(validationConsumer).accept(eq(password));
        //when/then
        Assertions.assertThatThrownBy(()->service.validateUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("some validation issue");
        Mockito.verify(validationConsumer).accept(eq(password));
    }

    @Test
    void shouldAcceptPasswordWhenValidatorAccepts() {
        //given
        String password = RandomStringUtils.insecure().nextAlphabetic(10);
        User user = new User("aaa", "aaa@ddf.com", password);
        //when//then
        Assertions.assertThatNoException()
                .isThrownBy(()->service.validateUser(user));

        Mockito.verify(validationConsumer).accept(eq(password));
    }
}