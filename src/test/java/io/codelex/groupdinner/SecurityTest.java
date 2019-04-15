package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.repository.service.AuthService;
import io.codelex.groupdinner.repository.service.Role;
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
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SecurityTest {

    AuthService service;

    @Autowired
    TestRestTemplate restTemplate;

    static final String email = "codelex@gmail.com";
    static final String password = "codelex";

    @Test
    public void customer_account_should_be_secured_by_default() {
        var result = restTemplate.getForEntity("/api/account", String.class);
        assertEquals(FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void customer_should_be_authorised_on_registration() {
        //given

        //when

        //then
//        //given
//        var result = register();
//        var sessionId = sessionId(result);
//        service.authorise(email, password, Role.REGISTERED_CLIENT);
//        //when
//        
//        //then
//        assertEquals(OK, result.getStatusCode());
//        assertEquals(OK, accessAccount(sessionId).getStatusCode());
    }

    private ResponseEntity<Void> register() {
        var uri = fromPath("/api/register")
                .queryParam("email", email)
                .queryParam("password", password)
                .build()
                .toUri();

        return restTemplate.postForEntity(uri, EMPTY, Void.class);
    }

    private ResponseEntity<Void> signIn() {
        var uri = fromPath("/api/sign-in")
                .queryParam("email", email)
                .queryParam("password", password)
                .build()
                .toUri();

        return restTemplate.postForEntity(uri, EMPTY, Void.class);
    }

    RegistrationRequest registrationRequest() {
        return new RegistrationRequest(
                "Marija",
                "Zālīte",
                "MZalite@gmail.com",
                "zalite123"
        );
    }

    private ResponseEntity<Void> signOut(String sessionId) {
        return restTemplate.exchange("/api/sign-out", POST, request(sessionId), Void.class);
    }

    private ResponseEntity<String> accessAccount(String sessionId) {
        return restTemplate.exchange("/api/account", GET, request(sessionId), String.class);
    }

    private ResponseEntity<String> accessAdminAccount(String sessionId) {
        return restTemplate.exchange("/admin-api/account", GET, request(sessionId), String.class);
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
