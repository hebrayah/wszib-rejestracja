package pl.edu.wszib.mbarczyk.rejestracja.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

@Slf4j
@RequiredArgsConstructor
public class PasswordValidationService extends AbstractValidationService{
    @Override
    protected void validateUser(User user) {

    }
}
