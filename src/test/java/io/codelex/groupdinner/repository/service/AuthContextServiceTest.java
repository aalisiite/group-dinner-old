package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthContextServiceTest {

    static final String firstName = "First";
    static final String lastName = "Last";
    static final String email = "email@email.com";
    static final String password = "Password123";
    static final RegistrationRequest registrationRequest = new RegistrationRequest(
            firstName,
            lastName,
            email,
            password
    );
    static final SignInRequest signInRequest = new SignInRequest(
            email,
            password
    );
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void customer_account_should_be_secured_by_default() {
        var result = restTemplate.getForEntity("/api/account", String.class);
        assertEquals(FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void customer_should_be_authorised_on_registration() {
        var result = register();
        assertEquals(CREATED, result.getStatusCode());

        var sessionId = sessionId(result);
        assertEquals(OK, accessAccount(sessionId).getStatusCode());
    }

    @Test
    public void customer_should_be_authorised_on_sign_in() {
        var result = register();
        assertEquals(CREATED, result.getStatusCode());

        result = signIn();
        assertEquals(OK, result.getStatusCode());

        var sessionId = sessionId(result);
        assertEquals(OK, accessAccount(sessionId).getStatusCode());
    }

    @Test
    public void customer_should_be_able_to_sign_out() {
        var result = register();
        assertEquals(CREATED, result.getStatusCode());

        var sessionId = sessionId(result);

        var resultSignOut = signOut(sessionId);
        assertEquals(OK, resultSignOut.getStatusCode());

        assertEquals(FORBIDDEN, accessAccount(sessionId).getStatusCode());
    }

    @Test
    public void customer_should_be_able_to_get_account_details() {
        var result = register();
        assertEquals(CREATED, result.getStatusCode());

        var sessionId = sessionId(result);
        assertEquals(email, accessAccount(sessionId).getBody());
    }

    private ResponseEntity<User> register() {
        var uri = fromPath("/api/register")
                .build()
                .toUri();
        return restTemplate.postForEntity(uri, registrationRequest, User.class);
    }

    private ResponseEntity<User> signIn() {
        var uri = fromPath("/api/log-in")
                .build()
                .toUri();
        return restTemplate.postForEntity(uri, signInRequest, User.class);
    }

    private ResponseEntity<Void> signOut(String sessionId) {
        return restTemplate.exchange("/api/sign-out", POST, request(sessionId), Void.class);
    }

    private ResponseEntity<String> accessAccount(String sessionId) {
        return restTemplate.exchange("/api/account", GET, request(sessionId), String.class);
    }

    private String sessionId(ResponseEntity<?> result) {
        return result.getHeaders().getFirst("Set-Cookie");
    }

    private HttpEntity<?> request(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", sessionId);
        return new HttpEntity<>(headers);
    }
}