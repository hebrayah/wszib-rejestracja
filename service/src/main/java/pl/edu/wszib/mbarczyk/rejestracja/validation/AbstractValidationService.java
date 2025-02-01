package pl.edu.wszib.mbarczyk.rejestracja.validation;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.util.function.UnaryOperator;

@Slf4j
public abstract class AbstractValidationService implements UnaryOperator<User> {

    private AbstractValidationService delegate;

    public void setDelegate(AbstractValidationService delegate) {
        this.delegate = delegate;
    }

    public AbstractValidationService withDelegate(AbstractValidationService delegate) {
        setDelegate(delegate);
        return this;
    }

    @Override
    public User apply(User user) {
        validateUser(user);
        if (null != delegate) {
            log.info("Call delegate: {}", delegate);
            delegate.apply(user);
        }
        return user;
    }

    protected abstract void validateUser(User user);

    @PostConstruct
    public void postConstruct(){
        log.info("Created with delegate: {}", delegate);
    }
}
