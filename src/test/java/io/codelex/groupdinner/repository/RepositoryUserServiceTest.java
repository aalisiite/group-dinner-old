package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class RepositoryUserServiceTest {

    private MapAttendeeRecordToAttendee toAttendee = Mockito.mock(MapAttendeeRecordToAttendee.class);
    private MapDinnerRecordToDinner toDinner = Mockito.mock(MapDinnerRecordToDinner.class);
    private DinnerRecordRepository dinnerRecordRepository = Mockito.mock(DinnerRecordRepository.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AttendeeRecordRepository attendeeRecordRepository = Mockito.mock(AttendeeRecordRepository.class);
    private RepositoryUserService userModule = new RepositoryUserService(dinnerRecordRepository, userRecordRepository, attendeeRecordRepository);
    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private UserRecord userRecord = createUserRecord();
    private String location = createLocation();
    private DinnerRecord dinnerRecord = createDinnerRecord();
    private CreateDinnerRequest dinnerRequest = createDinnerRequest();
    private AttendeeRecord attendeeRecord = createAcceptedAttendeeRecord();
    private User user = createUser();
    private CreateDinnerRequest request = createDinnerRequest();

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

        //then
        assertEquals(dinner.getId(), result.getId());
    }


    @Test
    public void should_be_able_to_join_event_with_accepted_status() {
        //given
        JoinDinnerRequest request = createJoinDinnerRequest();
        Attendee attendee = createAcceptedAttendee();
        Principal principal = () -> "1";


        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(attendeeRecord);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(attendee);


        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertTrue(result.getIsAccepted());
    }

/*
    @Test
    public void should_be_able_to_join_event_with_pending_status() {
        //given
        dinner.setCurrentGuests(dinner.getMaxGuests() + 1);
        int initialGuestCount = dinner.getCurrentGuests();
        JoinDinnerRequest request = new JoinDinnerRequest(
                user,
                dinner
        );

        //when
        Mockito.when(dinnerService.getDinner(any()))
                .thenReturn(Optional.of(dinner));

        Attendee result = userModule.joinDinner(request);

        //then
        assertEquals(initialGuestCount + 1, dinner.getCurrentGuests());
        assertFalse(result.getIsAccepted);
    }
*/

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
                "berzins@gmai.com"
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

    private JoinDinnerRequest createJoinDinnerRequest() {
        Dinner dinner = createDinner();
        return new JoinDinnerRequest(
                user,
                dinner
        );
    }

}