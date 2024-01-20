package com.piotrholda.spring.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldRegister() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "adamk", "pass");

        // when
        AuthenticationResponse response = testRestTemplate.postForObject("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);

        // then
        assertThat(response.getToken()).isNotBlank();
    }

    @Test
    void shouldNotRegisterWithEmptyLogin() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "", "pass");

        // when
        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldNotRegisterWithEmptyPassword() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "adamk", "");

        // when
        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldLogin() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "adamk", "pass");
        LoginRequest loginRequest = new LoginRequest("adamk", "pass");
        testRestTemplate.postForObject("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);

        // when
        AuthenticationResponse response = testRestTemplate.postForObject("http://localhost:" + port + "/api/v1/auth/login", loginRequest, AuthenticationResponse.class);

        // then
        assertThat(response.getToken()).isNotBlank();
    }

    @Test
    void shouldNotLoginWithWrongPassword() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "adamk", "pass");
        LoginRequest loginRequest = new LoginRequest("adamk", "wrong password");
        testRestTemplate.postForObject("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);

        // when
        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("http://localhost:" + port + "/api/v1/auth/login", loginRequest, AuthenticationResponse.class);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}