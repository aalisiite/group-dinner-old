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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class RepositoryUserServiceTest {

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private MapAttendeeRecordToAttendee toAttendee = Mockito.mock(MapAttendeeRecordToAttendee.class);
    private MapDinnerRecordToDinner toDinner = Mockito.mock(MapDinnerRecordToDinner.class);
    private DinnerRecordRepository dinnerRecordRepository = Mockito.mock(DinnerRecordRepository.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AttendeeRecordRepository attendeeRecordRepository = Mockito.mock(AttendeeRecordRepository.class);
    private FeedbackRecordRepository feedbackRecordRepository = Mockito.mock(FeedbackRecordRepository.class);
    private RepositoryUserService userModule = new RepositoryUserService(dinnerRecordRepository, userRecordRepository, attendeeRecordRepository, feedbackRecordRepository, passwordEncoder);
    
    
    private final TestVariableGenerator generator = new TestVariableGenerator();
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private User user2 = generator.getUserFromUserRecord(2L, userRecord2);
    
    private CreateDinnerRequest createDinnerRequest = generator.createDinnerRequest(location, localDateTime);
    
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1,location,localDateTime);
    private Dinner dinner = generator.getDinnerFromDinnerRecord(1L, dinnerRecord);
    
    private AttendeeRecord acceptedAttendeeRecord1 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord acceptedAttendeeRecord2 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord2);
    private AttendeeRecord pendingAttendeeRecord1 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord pendingAttendeeRecord2 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord2);
    
    private Attendee acceptedAttendee1 = generator.getAttendeeFromAttendeeRecord(1L, acceptedAttendeeRecord1);
    private Attendee acceptedAttendee2 = generator.getAttendeeFromAttendeeRecord(2L, acceptedAttendeeRecord2);
    private Attendee pendingAttendee1 = generator.getAttendeeFromAttendeeRecord(3L, pendingAttendeeRecord1);
    private Attendee pendingAttendee2 = generator.getAttendeeFromAttendeeRecord(4L, pendingAttendeeRecord2);


    @Test
    public void should_be_able_to_create_dinner() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(dinnerRecordRepository.isDinnerPresent(any(), any(), any(), any(), any(), any()))
                .thenReturn(false);
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));
        Mockito.when(dinnerRecordRepository.save(any()))
                .thenReturn(dinnerRecord);
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(acceptedAttendeeRecord1);
        Mockito.when(toDinner.apply(any()))
                .thenReturn(dinner);

        Dinner result = userModule.createDinner(user1.getId().toString(), createDinnerRequest);

        //then
        assertEquals(dinner.getTitle(), result.getTitle());
        assertEquals(dinner.getCreator(), result.getCreator());
    }

    @Test
    public void should_not_be_able_to_create_duplicate_dinner() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(dinnerRecordRepository.isDinnerPresent(any(), any(), any(), any(), any(), any()))
                .thenReturn(true);

        //then
        Executable executable = () -> userModule.createDinner(user1.getId().toString(), createDinnerRequest);
        assertThrows(IllegalStateException.class, executable);
    }


    @Test
    public void should_be_able_to_join_event_with_accepted_status() {
        //given
        Principal principal = () -> "1";

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(acceptedAttendeeRecord1);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(acceptedAttendee1);


        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertTrue(result.isAccepted());
    }


    @Test
    public void should_be_able_to_join_event_with_pending_status() {
        //given
        dinnerRecord.setMaxGuests(1);
        Principal principal = () -> "1";

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(acceptedAttendeeRecord1);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(acceptedAttendee1);


        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertFalse(result.isAccepted());
    }

    @Test
    public void should_be_able_to_leave_feedback() {
        //todo
    }

    @Test
    public void should_not_be_able_to_leave_feedback_two_times_for_one_person() {
        //todo
    }




}