package io.codelex.groupdinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.CreateDinnerRequest;
import io.codelex.groupdinner.api.request.LeaveFeedbackRequest;
import io.codelex.groupdinner.configuration.WebConfiguration;
import io.codelex.groupdinner.repository.TestVariableGenerator;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import io.codelex.groupdinner.repository.service.DinnerService;
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
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebConfiguration.class)
@WithMockUser(roles = {"REGISTERED_CLIENT"})
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
    DinnerService service;
    @Autowired
    private MockMvc mockMvc;

    private String location = generator.createLocation();
    private LocalDateTime localDateTime = generator.createDateTime();
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private User user2 = generator.getUserFromUserRecord(2L, userRecord2);
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private Dinner dinner = generator.getDinnerFromDinnerRecord(1L, dinnerRecord);
    private AttendeeRecord attendeeRecord = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord1);
    private Attendee attendee = generator.getAttendeeFromAttendeeRecord(1L, attendeeRecord);
    private CreateDinnerRequest createDinnerRequest = generator.createDinnerRequest(location, localDateTime);
    private LeaveFeedbackRequest leaveFeedbackRequest = generator.createLeaveFeedbackRequest(user2);
    private FeedbackRecord feedbackRecord = generator.createGoodFeedbackRecord(dinnerRecord, userRecord1, userRecord2);
    private Feedback feedback = generator.getFeedbackFromFeedbackRecord(1L, feedbackRecord);

    @Test
    public void create_dinner_should_return_dinner_and_status_created() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(createDinnerRequest);

        //when
        Mockito.when(service.createDinner("user", createDinnerRequest))
                .thenReturn(dinner);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/create-dinner")
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
        assertEquals(dinner.getCreator().getId(), result.getCreator().getId());
        assertEquals(dinner.getDateTime(), result.getDateTime());
    }

    @Test
    public void join_dinner_should_return_attendee_and_status_ok() throws Exception {

        //when
        Mockito.when(service.joinDinner("user", 1L))
                .thenReturn(attendee);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/dinners/1/join")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Attendee result = MAPPER.readValue(
                jsonResponse, new TypeReference<Attendee>() {
                }
        );

        //then
        assertEquals(attendee.getDinner().getTitle(), result.getDinner().getTitle());
        assertEquals(attendee.getUser().getEmail(), result.getUser().getEmail());
        assertEquals(attendee.isAccepted(), result.isAccepted());
    }


    @Test
    public void leave_feedback_should_return_feedback_and_status_created() throws Exception {
        //given
        String jsonRequest = MAPPER.writeValueAsString(leaveFeedbackRequest);

        //when
        Mockito.when(service.leaveFeedback("user", 1L, leaveFeedbackRequest))
                .thenReturn(feedback);

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/dinners/1/feedback")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Feedback result = MAPPER.readValue(
                jsonResponse, new TypeReference<Feedback>() {
                }
        );

        //then
        assertEquals(feedback.getDinner().getTitle(), result.getDinner().getTitle());
        assertEquals(feedback.getProvider().getEmail(), result.getProvider().getEmail());
        assertEquals(feedback.getReceiver().getEmail(), result.getReceiver().getEmail());
        assertEquals(feedback.isRating(), result.isRating());
    }

    @Test
    public void get_dinner_attendees_should_return_attendees_and_status_ok() throws Exception {
        //when
        Mockito.when(service.findDinnerAttendees(1L, true))
                .thenReturn(List.of(user1, user2));

        //expect
        String jsonResponse = mockMvc.perform(
                get("/api/dinners/1/attendees?accepted=true")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> result = MAPPER.readValue(
                jsonResponse, new TypeReference<List<User>>() {
                }
        );

        //then
        assertEquals(user1.getEmail(), result.get(0).getEmail());
        assertEquals(user2.getEmail(), result.get(1).getEmail());
    }

    @Test
    public void get_dinners_should_return_dinners_and_status_ok() throws Exception {
        //when
        Mockito.when(service.getGoodMatchDinners("user"))
                .thenReturn(List.of(dinner));

        //expect
        String jsonResponse = mockMvc.perform(
                get("/api/dinners")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Dinner> result = MAPPER.readValue(
                jsonResponse, new TypeReference<List<Dinner>>() {
                }
        );

        //then
        assertEquals(dinner.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void illegal_argument_exception_should_always_return_status_bad_request() throws Exception {

        //when
        Mockito.when(service.joinDinner("user", 1L))
                .thenThrow(new IllegalArgumentException());

        //expect
        mockMvc.perform(
                post("/api/dinners/1/join")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


}