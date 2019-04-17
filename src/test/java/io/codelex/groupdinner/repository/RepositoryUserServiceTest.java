package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.mapper.MapAttendeeRecordToAttendee;
import io.codelex.groupdinner.repository.mapper.MapDinnerRecordToDinner;
import io.codelex.groupdinner.repository.mapper.MapFeedbackRecordToFeedback;
import io.codelex.groupdinner.repository.mapper.MapUserRecordToUser;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
    private MapFeedbackRecordToFeedback toFeedback = Mockito.mock(MapFeedbackRecordToFeedback.class);
    private MapUserRecordToUser toUser = Mockito.mock(MapUserRecordToUser.class);
    private DinnerRecordRepository dinnerRecordRepository = Mockito.mock(DinnerRecordRepository.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AttendeeRecordRepository attendeeRecordRepository = Mockito.mock(AttendeeRecordRepository.class);
    private FeedbackRecordRepository feedbackRecordRepository = Mockito.mock(FeedbackRecordRepository.class);
    private RepositoryUserService userModule = new RepositoryUserService(dinnerRecordRepository, userRecordRepository, attendeeRecordRepository, feedbackRecordRepository, passwordEncoder);


    private final TestVariableGenerator generator = new TestVariableGenerator();
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    private String encodedPassword = "encodedPassword";
    
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private User user2 = generator.getUserFromUserRecord(2L, userRecord2);

    private CreateDinnerRequest createDinnerRequest = generator.createDinnerRequest(location, localDateTime);

    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private Dinner dinner = generator.getDinnerFromDinnerRecord(1L, dinnerRecord);

    private AttendeeRecord acceptedAttendeeRecord1 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord acceptedAttendeeRecord2 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord2);
    private AttendeeRecord pendingAttendeeRecord1 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord pendingAttendeeRecord2 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord2);

    private Attendee acceptedAttendee1 = generator.getAttendeeFromAttendeeRecord(1L, acceptedAttendeeRecord1);
    private Attendee acceptedAttendee2 = generator.getAttendeeFromAttendeeRecord(2L, acceptedAttendeeRecord2);
    private Attendee pendingAttendee1 = generator.getAttendeeFromAttendeeRecord(3L, pendingAttendeeRecord1);
    private Attendee pendingAttendee2 = generator.getAttendeeFromAttendeeRecord(4L, pendingAttendeeRecord2);

    private FeedbackRecord goodFeedbackRecord = generator.createGoodFeedbackRecord(dinnerRecord, userRecord1, userRecord2);
    private Feedback goodFeedback = generator.getFeedbackFromFeedbackRecord(1L, goodFeedbackRecord);

    private LeaveFeedbackRequest leaveFeedbackRequest = generator.createLeaveFeedbackRequest();
    private RegistrationRequest registrationRequest = generator.createRegistrationRequest();
    private SignInRequest signInRequest = generator.createSignInRequest();
    
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
        Mockito.when(dinnerRecordRepository.getDinner(any(), any(), any(), any(), any(), any()))
                .thenReturn(dinnerRecord);
        Mockito.when(toDinner.apply(any()))
                .thenReturn(dinner);

        Dinner result = userModule.createDinner(user1.getId().toString(), createDinnerRequest);

        //then
        assertEquals(dinner.getTitle(), result.getTitle());
        assertEquals(dinner.getCreator(), result.getCreator());
    }

    @Test
    public void should_be_able_to_register_as_new_user () {
        //when
        Mockito.when(userRecordRepository.isUserPresent(any()))
                .thenReturn(false);
        Mockito.when(passwordEncoder.encode(any()))
                .thenReturn(encodedPassword);
        Mockito.when(userRecordRepository.save(any()))
                .thenReturn(userRecord1);
        Mockito.when(toUser.apply(any()))
                .thenReturn(user1);
        
        User result = userModule.registerUser(registrationRequest);
        
        //then
        assertEquals(result, user1);
    }
    
    @Test
    public void should_not_be_able_to_register_with_existing_email () {
        //when
        Mockito.when(userRecordRepository.isUserPresent(any()))
                .thenReturn(true);
        
        //then
        Executable executable = () -> userModule.registerUser(registrationRequest);
        assertThrows(IllegalStateException.class, executable, "Email already exists");
    }
//todo change all returns to result
    @Test
    public void should_be_able_to_sign_in () {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        Mockito.when(toUser.apply(any()))
                .thenReturn(user1);
        
        
    }
//todo
    @Test
    public void should_not_be_able_to_sign_in_with_wrong_password () {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);
        
    }
    
    @Test
    public void should_be_able_to_join_event_with_accepted_status() {
        //given
        Principal principal = () -> userRecord1.getEmail();

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(attendeeRecordRepository.userJoinedDinner(any(), any()))
                .thenReturn(false);
        Mockito.when(attendeeRecordRepository.countDinnerAttendees(any()))
                .thenReturn(1);
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
        Principal principal = () -> userRecord1.getEmail();
        dinnerRecord.setMaxGuests(1);

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(attendeeRecordRepository.userJoinedDinner(any(), any()))
                .thenReturn(false);
        Mockito.when(attendeeRecordRepository.countDinnerAttendees(any()))
                .thenReturn(1);
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));
        Mockito.when(attendeeRecordRepository.save(any()))
                .thenReturn(pendingAttendeeRecord1);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(acceptedAttendee1);

        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertFalse(result.isAccepted());
    }


    @Test
    public void should_not_be_able_to_join_dinner_twice() {
        //given
        Principal principal = () -> userRecord1.getEmail();

        //when
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(attendeeRecordRepository.userJoinedDinner(any(), any()))
                .thenReturn(true);
        Mockito.when(attendeeRecordRepository.getAttendee(any(), any()))
                .thenReturn(acceptedAttendeeRecord1);
        Mockito.when(toAttendee.apply(any()))
                .thenReturn(acceptedAttendee1);

        Attendee result = userModule.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertTrue(result.isAccepted());
    }


    @Test
    public void should_be_able_to_leave_feedback() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ == 1) {
                    return userRecord1;
                }
                return userRecord2;
            }
        });
        Mockito.when(feedbackRecordRepository.isFeedbackPresent(any(), any(), any()))
                .thenReturn(false);
        Mockito.when(attendeeRecordRepository.userJoinedDinner(any(), any()))
                .thenReturn(true);
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(feedbackRecordRepository.save(any()))
                .thenReturn(goodFeedbackRecord);
        Mockito.when(toFeedback.apply(any()))
                .thenReturn(goodFeedback);
        
        Feedback result = userModule.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);
        
        //then
        assertEquals(result, goodFeedback);
    }


    @Test
    public void should_not_be_able_to_leave_feedback_for_yourself () {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        
        //then
        Executable executable = () -> userModule.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);
        assertThrows(IllegalStateException.class, executable, "Provider is same as receiver");
    }

    @Test
    public void should_not_be_able_to_leave_feedback_if_did_not_attend_same_dinner () {
        //when
        Mockito.when(userRecordRepository.findByEmail(any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ == 1) {
                    return userRecord1;
                }
                return userRecord2;
            }
        });
        Mockito.when(feedbackRecordRepository.isFeedbackPresent(any(), any(), any()))
                .thenReturn(false);
        Mockito.when(attendeeRecordRepository.userJoinedDinner(any(), any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ == 1) {
                    return true;
                }
                return false;
            }
        });

        //then
        Executable executable = () -> userModule.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);
        assertThrows(IllegalStateException.class, executable, "No such common dinner for users");
    }

    @Test
    public void should_not_be_able_to_leave_duplicate_feedback () {
        //when
        Mockito.when(userRecordRepository.findByEmail(any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ == 1) {
                    return userRecord1;
                }
                return userRecord2;
            }
        });
        Mockito.when(feedbackRecordRepository.isFeedbackPresent(any(), any(), any()))
                .thenReturn(true);
        Mockito.when(feedbackRecordRepository.getFeedback(any(), any(), any()))
                .thenReturn(goodFeedbackRecord);
        Mockito.when(toFeedback.apply(any()))
                .thenReturn(goodFeedback);

        Feedback result = userModule.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);

        //then
        assertEquals(result, goodFeedback);
    }


}