package pl.edu.wszib.mbarczyk.rejestracja;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;
import pl.edu.wszib.mbarczyk.rejestracja.validation.AbstractValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.UsernameValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.email.EmailDomainExistenceValidator;
import pl.edu.wszib.mbarczyk.rejestracja.validation.email.EmailFormatValidatingProxy;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Configuration
public class RegistrationConfiguration {

    @Bean
    public UnaryOperator<User> userValidationService(@Qualifier("emailValidationService") AbstractValidationService emailValidationService){
        return new UsernameValidationService().withDelegate(emailValidationService);
    }

    @Bean
    public AbstractValidationService emailValidationService(@Qualifier("emailFormatValidationProxy") Consumer<String> emailFormatValidationProxy){
        return new EmailValidationService(emailFormatValidationProxy);
    }

    @Bean
    public Consumer<String> emailFormatValidationProxy(@Qualifier("domainExistenceValidator") Consumer<String> domainExistenceValidator) {
        return new EmailFormatValidatingProxy(domainExistenceValidator);
    }

    @Bean
    public Consumer<String> domainExistenceValidator(@Value("${email.dns.server}") String dnsServer) {
        return new EmailDomainExistenceValidator(dnsServer);
    }
}
