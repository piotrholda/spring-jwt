package com.piotrholda.spring.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerTest {

    @LocalServerPort
    @SuppressWarnings("unused")
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnUserProfile() {

        // given
        RegisterRequest registerRequest = new RegisterRequest("adam", "kowalski", "adamk", "pass");
        AuthenticationResponse authenticationResponse = testRestTemplate.postForObject("http://localhost:" + port + "/api/v1/auth/register", registerRequest, AuthenticationResponse.class);
        String token = authenticationResponse.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // when
        ResponseEntity<ProfileResponse> response = testRestTemplate.exchange("http://localhost:" + port + "/api/v1/profile", HttpMethod.GET, request, ProfileResponse.class);

        // then
        assertThat(response.getBody()).isEqualTo(new ProfileResponse("adam", "kowalski", "adamk", Role.ROLE_USER));
    }


}