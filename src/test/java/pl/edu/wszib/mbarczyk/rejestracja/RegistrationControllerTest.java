package pl.edu.wszib.mbarczyk.rejestracja;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import pl.edu.wszib.mbarczyk.rejestracja.model.RegistrationRequest;
import pl.edu.wszib.mbarczyk.rejestracja.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class RegistrationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    RegistrationController registrationController;
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldHandleBasicRequest() {
        //given
        RegistrationRequest request = new RegistrationRequest(List.of(new User("nazwa", "kicia@gmail.com", "M3dh!a.asdEr")));
        //when
        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk();
        //then
    }

    @Test
    void shouldFailOnTooSimplePassword() {
        //given
        RegistrationRequest request = new RegistrationRequest(List.of(new User("nazwa", "kicia@gmail.com", "aaa")));
        //when
        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(aaa->log.info("{}", new String( aaa.getResponseBody())))
                .jsonPath("$.error.messaage").isNotEmpty();
        //then
    }


    @Test
    void ShouldReturnBadRequestOnEmptyList() {
        //given
        //when
        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus()
                .isBadRequest();
        //then
    }

    @Test
    void shouldReturnBadRequestOnEmptyRequest() {
        //given
        //when/then
        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
        //then
    }

    @Test
    void shouldHandleRequestWithMultipleUsers() {
        //given
        RegistrationRequest request = new RegistrationRequest(
                IntStream.range(2, 7)
                .mapToObj(i->createRandomUser())
                .toList()
        );
        //when
        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk();
        //then


    }

    private User createRandomUser() {
        return new User(RandomStringUtils.insecure().nextAlphabetic(6),
                createRandomEmail(),
                RandomStringUtils.insecure().nextAlphanumeric(9) + "!"
        );
    }

    private String createRandomEmail() {
        return String.format("%s@%s.%s", RandomStringUtils.insecure().nextAlphanumeric(8),
                "cnn", "com");
    }
}