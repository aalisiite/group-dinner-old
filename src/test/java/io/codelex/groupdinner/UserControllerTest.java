package io.codelex.groupdinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.repository.model.Dinner;
import io.codelex.groupdinner.repository.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebConfiguration.class)
public class UserControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private UserModule userModule = Mockito.mock(UserModule.class);
    private User user = createUser();
    private String location = createLocation();
    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private CreateDinnerRequest request = createDinnerRequest(user, location, localDateTime);
    private Dinner dinner = createDinner();


    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        builder.modules(javaTimeModule);
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.registerModule(javaTimeModule);
    }


    @Test
    public void should_return_dinner_and_status_created() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(request);


        //when
        Mockito.when(userModule.createDinner(any()))
                .thenReturn(dinner);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/dinners")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Dinner result = MAPPER.readValue(
                jsonResponse, new TypeReference<Dinner>() {
                }
        );

        //then
        assertEquals(dinner, result);
    }
    

    private User createUser() {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
        );
    }

    private CreateDinnerRequest createDinnerRequest(User user, String location, LocalDateTime localDateTime) {
        return new CreateDinnerRequest(
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private Dinner createDinner() {
        return new Dinner(
                1L,
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }


    private String createLocation() {
        return "Jurmalas Gatve 76";
    }

}