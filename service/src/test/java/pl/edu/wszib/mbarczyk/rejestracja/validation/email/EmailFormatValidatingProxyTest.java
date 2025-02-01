package pl.edu.wszib.mbarczyk.rejestracja.validation.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationException;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class EmailFormatValidatingProxyTest {

    @Mock
    Consumer<String> dnsChecker;

    @InjectMocks
    EmailFormatValidatingProxy emailFormatValidatingProxy;

    @Test
    void shouldAcceptCorrectEmailAddress() {
        //given
        String email = "mbarczyk@student.wszib.edu.pl";
        //when
        Assertions.assertThatNoException()
                .isThrownBy(()->emailFormatValidatingProxy.accept(email));
        //then
        Mockito.verify(dnsChecker).accept(eq(email));
    }

    @Test
    void shouldRejectAddressWhenTargetThrowsError() {
        //given
        EmailValidationException thrown = new EmailValidationException("something");
        String email = "mbarczyk@student.wszib.edu.pl";
        Mockito.doThrow(thrown)
                .when(dnsChecker)
                .accept(eq(email));
        //when
        Assertions.assertThatThrownBy(()->emailFormatValidatingProxy.accept(email))
                .isInstanceOf(EmailValidationException.class)
                .isEqualTo(thrown);
        //then
        Mockito.verify(dnsChecker).accept(eq(email));
    }


    @ParameterizedTest
    @ValueSource(strings = {"xzcxcxzc", "2435423@", "mąśćd@ma.pl", "dsaflkjasdlkfjsadlfkj@fasdfa", "hermenegilda.kociubinska@galczyński.polska" })
    void shouldRejectMalformedEmailAddressesNotCallDelegate(String email) {
        //when
        Assertions.assertThatThrownBy(()->emailFormatValidatingProxy.accept(email))
                .isInstanceOf(EmailValidationException.class);
        //then
        Mockito.verifyNoInteractions(dnsChecker);
    }
}