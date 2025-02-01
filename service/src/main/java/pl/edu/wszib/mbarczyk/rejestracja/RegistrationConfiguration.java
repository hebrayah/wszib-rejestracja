package pl.edu.wszib.mbarczyk.rejestracja;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;
import pl.edu.wszib.mbarczyk.rejestracja.validation.AbstractValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.EmailValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.PasswordValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.UsernameValidationService;
import pl.edu.wszib.mbarczyk.rejestracja.validation.email.EmailDomainExistenceValidator;
import pl.edu.wszib.mbarczyk.rejestracja.validation.email.EmailFormatValidatingProxy;
import pl.edu.wszib.mbarczyk.rejestracja.validation.password.CompromisedPasswordCheckerValidator;
import pl.edu.wszib.mbarczyk.rejestracja.validation.password.PasswordFormatValidatingProxy;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Configuration
public class RegistrationConfiguration {

    @Bean
    public UnaryOperator<User> userValidationService(@Qualifier("emailValidationService") AbstractValidationService emailValidationService) {
        return new UsernameValidationService().withDelegate(emailValidationService);
    }

    @Bean
    public AbstractValidationService emailValidationService(@Qualifier("emailFormatValidationProxy") Consumer<String> emailFormatValidationProxy,
                                                            @Qualifier("passwordValidationService") AbstractValidationService passwordValidationService

    ) {
        return new EmailValidationService(emailFormatValidationProxy).withDelegate(passwordValidationService);
    }

    @Bean
    public Consumer<String> emailFormatValidationProxy(@Qualifier("domainExistenceValidator") Consumer<String> domainExistenceValidator) {
        return new EmailFormatValidatingProxy(domainExistenceValidator);
    }

    @Bean
    public Consumer<String> domainExistenceValidator(@Value("${email.dns.server}") String dnsServer) {
        return new EmailDomainExistenceValidator(dnsServer);
    }

    @Bean(name = "passwordValidationService")
    public AbstractValidationService passwordValidationService(@Qualifier("passwordFormatValidatingProxy") Consumer<String> passwordFormatValidatingProxy) {
        return new PasswordValidationService(passwordFormatValidatingProxy);
    }

    @Bean
    public Consumer<String> passwordFormatValidatingProxy(@Qualifier("compromisedPasswordValidationConsumer") Consumer<String> compromisedPasswordValidationConsumer) {
        return new PasswordFormatValidatingProxy(compromisedPasswordValidationConsumer);
    }

    @Bean
    public Consumer<String> compromisedPasswordValidationConsumer(RestClient restClient) {
        return new CompromisedPasswordCheckerValidator(restClient);
    }

    @Bean
    public RestClient restClient(@Value("${password.validation.service.url}") String validationServiceUrl) {
        return RestClient.builder()
                .baseUrl(validationServiceUrl)
                .defaultHeader("User-Agent", "WSZiB Test PasswordCheck")
                .build();
    }
}
