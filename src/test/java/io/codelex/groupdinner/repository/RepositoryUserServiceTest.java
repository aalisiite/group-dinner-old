package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.mapper.MapAttendeeRecordToAttendee;
import io.codelex.groupdinner.repository.mapper.MapDinnerRecordToDinner;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class RepositoryUserServiceTest {

    private MapAttendeeRecordToAttendee toAttendee = Mockito.mock(MapAttendeeRecordToAttendee.class);
    private MapDinnerRecordToDinner toDinner = Mockito.mock(MapDinnerRecordToDinner.class);
    private DinnerRecordRepository dinnerRecordRepository = Mockito.mock(DinnerRecordRepository.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AttendeeRecordRepository attendeeRecordRepository = Mockito.mock(AttendeeRecordRepository.class);
    private FeedbackRecordRepository feedbackRecordRepository = Mockito.mock(FeedbackRecordRepository.class);
    private RepositoryUserService userModule = new RepositoryUserService(dinnerRecordRepository, userRecordRepository, attendeeRecordRepository, feedbackRecordRepository);
    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private UserRecord userRecord = createUserRecord();
    private String location = createLocation();
    private DinnerRecord dinnerRecord = createDinnerRecord();
    private AttendeeRecord attendeeRecord = createAcceptedAttendeeRecord();
    private User user = createUser();
    private CreateDinnerRequest dinnerRequest = createDinnerRequest();

    @Test
    public void should_be_able_to_create_dinner() {
        //given
        Dinner dinner = createDinner();

        //when
        Mockito.when(dinnerRecordRepository.isDinnerPresent(any(), any(), any(), any(), any(), any()))
                .thenReturn(false);
        Mockito.when(dinnerRecordRepository.save(any()))
                .thenReturn(dinnerRecord);
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(attendeeRecord);
        Mockito.when(toDinner.apply(any()))
                .thenReturn(dinner);

        Dinner result = userModule.createDinner(dinnerRequest);
        result.setId(dinner.getId());
        result.getCreator().setId(dinner.getCreator().getId());

        //then
        assertEquals(dinner.getTitle(), result.getTitle());
        assertEquals(dinner.getCreator(), result.getCreator());
    }

    @Test
    public void should_not_be_able_to_create_duplicate_dinner() {
        //when
        Mockito.when(dinnerRecordRepository.isDinnerPresent(any(), any(), any(), any(), any(), any()))
                .thenReturn(true);

        //then
        Executable executable = () -> userModule.createDinner(dinnerRequest);
        assertThrows(IllegalStateException.class, executable);
    }


    @Test
    public void should_be_able_to_join_event_with_accepted_status() {
        //given
        Attendee attendee = createAcceptedAttendee();
        Principal principal = () -> "1";

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(attendeeRecord);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(attendee);


        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertTrue(result.getIsAccepted());
    }


    @Test
    public void should_be_able_to_join_event_with_pending_status() {
        //given
        dinnerRecord.setCurrentGuests(dinnerRecord.getMaxGuests());
        int initialGuestCount = dinnerRecord.getCurrentGuests();
        Attendee attendee = createAcceptedAttendee();
        Principal principal = () -> "1";

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(attendeeRecord);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(attendee);


        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertFalse(result.getIsAccepted());
    }
    
    @Test
    public void should_be_able_to_leave_feedback () {
        //todo
    }
    
    @Test
    public void should_not_be_able_to_leave_feedback_two_times_for_one_person () {
        //todo
    }

    private AttendeeRecord createAcceptedAttendeeRecord() {
        return new AttendeeRecord(
                dinnerRecord,
                userRecord,
                true
        );
    }

    private Attendee createAcceptedAttendee() {
        return new Attendee(
                1L,
                createDinner(),
                user,
                true
        );
    }

    private UserRecord createUserRecord() {
        return new UserRecord(
                "Janis",
                "Berzins",
                "berzins@gmai.com",
                "password"
        );
    }

    private CreateDinnerRequest createDinnerRequest() {
        return new CreateDinnerRequest(
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private DinnerRecord createDinnerRecord() {
        return new DinnerRecord(
                "This is a title",
                userRecord,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private String createLocation() {
        return "Jurmalas Gatve 76";
    }

    private User createUser() {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
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

}