package pl.edu.wszib.mbarczyk.rejestracja;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.wszib.mbarczyk.rejestracja.model.RegistrationRequest;
import pl.edu.wszib.mbarczyk.rejestracja.model.RegistrationResponse;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;
import pl.edu.wszib.mbarczyk.rejestracja.validation.ValidationException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class RegistrationController {

    private final UnaryOperator<User> userValidationService;

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<RegistrationResponse> registerUsers(@RequestBody RegistrationRequest request) {
        log.info("Request: {}", request);
        return Mono.fromSupplier(() -> processRequest(request.getRequestedUsers()));
    }

    private RegistrationResponse processRequest(List<User> requestedUsers) {
        try {
            return new RegistrationResponse(Optional.ofNullable(requestedUsers)
                    .orElseThrow()
                    .stream()
                    .parallel()
                    .map(userValidationService)
                    .collect(Collectors.toMap(user -> registerUser(user), Function.identity()))
            );
        } catch (NoSuchElementException nosee) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista użytkowników jest pusta", nosee  );
        }
    }

    private String registerUser(User user) {
        log.info("Requested user to register: {}", user);
        return UUID.randomUUID().toString();
    }
}
