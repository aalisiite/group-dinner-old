package io.codelex.groupdinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import io.codelex.groupdinner.repository.TestVariableGenerator;
import io.codelex.groupdinner.repository.model.UserRecord;
import io.codelex.groupdinner.repository.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final TestVariableGenerator generator = new TestVariableGenerator();
    @MockBean
    AuthService service;
    @Autowired
    private MockMvc mockMvc;

    private UserRecord userRecord1 = generator.createUserRecord1();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private RegistrationRequest registrationRequest = generator.createRegistrationRequest();
    private SignInRequest signInRequest = generator.createSignInRequest();


    @Test
    public void sign_in_should_return_user_and_status_ok() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(signInRequest);

        //when
        Mockito.when(service.authenticateUser(signInRequest))
                .thenReturn(user1);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/sign-in")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User result = MAPPER.readValue(
                jsonResponse, new TypeReference<User>() {
                }
        );

        //then
        assertEquals(user1.getEmail(), result.getEmail());
    }

    @Test
    public void register_should_return_user_and_status_created() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(registrationRequest);

        //when
        Mockito.when(service.registerUser(registrationRequest))
                .thenReturn(user1);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/register")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User result = MAPPER.readValue(
                jsonResponse, new TypeReference<User>() {
                }
        );

        //then
        assertEquals(user1.getEmail(), result.getEmail());
    }
}