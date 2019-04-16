package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.repository.service.AuthService;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import io.codelex.groupdinner.repository.service.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
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

    AuthService service = new AuthService();
    @Autowired
    RepositoryUserService userService;

    @Autowired
    TestRestTemplate restTemplate;

    private static final String email = "codelex@gmail.com";
    private static final String password = "codelex";

    @Test
    public void customer_account_should_be_secured_by_default() {
        var result = restTemplate.getForEntity("/api/account", String.class);
        assertEquals(FORBIDDEN, result.getStatusCode());
    }
    
}
