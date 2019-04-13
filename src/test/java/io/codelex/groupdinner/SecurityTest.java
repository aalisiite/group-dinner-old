package io.codelex.groupdinner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SecurityTest {

    @Autowired
    TestRestTemplate restTemplate;

    static final String email = "codelex@gmail.com";
    static final String password = "codelex";
    
    @Test
    public void customer_account_should_be_secured_by_default() {
        var result = restTemplate.getForEntity("/api/account", String.class);
        assertEquals(FORBIDDEN, result.getStatusCode());
    }
    
}
