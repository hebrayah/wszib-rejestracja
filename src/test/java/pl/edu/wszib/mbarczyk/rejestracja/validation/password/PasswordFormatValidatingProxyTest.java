package pl.edu.wszib.mbarczyk.rejestracja.validation.password;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wszib.mbarczyk.rejestracja.validation.PasswordValidationException;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class PasswordFormatValidatingProxyTest {

    @Mock
    Consumer<String> consumer;

    @InjectMocks
    PasswordFormatValidatingProxy proxy;

    @Test
    void shouldAcceptComplexPasswordAndCallConsumer() {
        //given
        String password = "De$5Asfjs4957S";
        //when
        //then
        Assertions.assertThatNoException().isThrownBy(()->proxy.accept(password));
        Mockito.verify(consumer).accept(eq(password));
    }

    @Test
    void shouldAcceptComplexPasswordAndLetConsumerThrow() {
        //given
        PasswordValidationException targetException = new PasswordValidationException("Some Exception");
        String password = "De$5Asfjs4957S";
        Mockito.doThrow(targetException).when(consumer).accept(eq(password));
        //when
        //then
        Assertions.assertThatThrownBy(()->proxy.accept(password))
                .isEqualTo(targetException);
        Mockito.verify(consumer).accept(eq(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "aaa", "aasdf1234", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "DfDfDfDfDfDFDFDfDf12", "Dd1", "DFG$1"})
    void shouldRejectTooSimplePasswords(String password) {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(()->proxy.accept(password))
                .isInstanceOf(PasswordValidationException.class);
        Mockito.verifyNoInteractions(consumer);

    }

}