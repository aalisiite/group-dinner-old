package io.codelex.groupdinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.configuration.WebConfiguration;
import io.codelex.groupdinner.repository.TestVariableGenerator;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebConfiguration.class)
@WithMockUser(roles={"REGISTERED_CLIENT"})
public class UserControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

    private final TestVariableGenerator generator = new TestVariableGenerator();
    @MockBean
    RepositoryUserService service;
    @Autowired
    private MockMvc mockMvc;
    private UserService userService = Mockito.mock(UserService.class);
    private String location = generator.createLocation();
    private LocalDateTime localDateTime = generator.createDateTime();
    private UserRecord userRecord1 = generator.createUserRecord1();
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private Dinner dinner = generator.getDinnerFromDinnerRecord(1L, dinnerRecord);
    private CreateDinnerRequest request = generator.createDinnerRequest(location, localDateTime);

    //todo
    @Test
    public void should_return_dinner_and_status_created() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(request);

        //when
        Mockito.when(userService.createDinner("user", request))
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

        DinnerRecord result = MAPPER.readValue(
                jsonResponse, new TypeReference<DinnerRecord>() {
                }
        );

        //then
        assertEquals(dinner.getCreator().getId(), result.getCreator().getId());
        assertEquals(dinner.getDateTime(), result.getDateTime());
    }

}