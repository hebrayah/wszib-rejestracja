package pl.edu.wszib.mbarczyk.rejestracja.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

class UsernameValidationServiceTest {
    UsernameValidationService service = new UsernameValidationService();

    @Test
    void shouldThrowExceptionOnTooShortUsername() {
        //given
        User user = new User("a", "a@a.pl", "sdfg");
        //when
        Assertions.assertThatThrownBy(()->service.apply(user)).hasNoCause().isInstanceOf(ValidationException.class)
                .hasMessageContaining("Nazwa użytkownika musi się składać z 3 do 10 znaków.");
        //then
    }

    @Test
    void shouldThrowExceptionOnTooLongUsername() {
        //given
        User user = new User("aascdfgrSDFrawdrt3456.sdfreasd", "a@a.pl", "sdfg");
        //when
        Assertions.assertThatThrownBy(()->service.apply(user)).hasNoCause().isInstanceOf(ValidationException.class)
                .hasMessageContaining("Nazwa użytkownika musi się składać z 3 do 10 znaków.");
        //then
    }

    @Test
    void shouldThrowExceptionOnInvalidCharsInUsername() {
        //given
        User user = new User("aas$%dsf", "a@a.pl", "sdfg");
        //when
        Assertions.assertThatThrownBy(()->service.apply(user)).hasNoCause().isInstanceOf(ValidationException.class)
                .hasMessageContaining("Nazwa użytkownika musi się składać z 3 do 10 znaków.");
        //then
    }

    @Test
    void shouldThrowExceptionOnEmptyUsername() {
        //given
        User user = new User(null, "a@a.pl", "sdfg");
        //when
        Assertions.assertThatThrownBy(()->service.apply(user)).hasNoCause().isInstanceOf(ValidationException.class)
                .hasMessageContaining("Nazwa użytkownika nie może być pusta.");
        //then
    }

    @Test
    void shouldAcceptCorrectUsername() {
        //given
        User user = new User("someName", "a@a.pl", "sdfg");
        //when
        Assertions.assertThatNoException()
                .isThrownBy(()->service.apply(user));
        //then
    }

}