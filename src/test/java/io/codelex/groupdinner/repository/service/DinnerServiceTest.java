package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.CreateDinnerRequest;
import io.codelex.groupdinner.api.request.LeaveFeedbackRequest;
import io.codelex.groupdinner.repository.*;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class DinnerServiceTest {

    private final TestVariableGenerator generator = new TestVariableGenerator();
    private MapDBRecordToApiCompatible toApiCompatible = Mockito.mock(MapDBRecordToApiCompatible.class);
    private DinnerRecordRepository dinnerRecordRepository = Mockito.mock(DinnerRecordRepository.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AttendeeRecordRepository attendeeRecordRepository = Mockito.mock(AttendeeRecordRepository.class);
    private FeedbackRecordRepository feedbackRecordRepository = Mockito.mock(FeedbackRecordRepository.class);
    private DinnerService dinnerService = new DinnerService(dinnerRecordRepository, userRecordRepository, attendeeRecordRepository, feedbackRecordRepository);
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private User user2 = generator.getUserFromUserRecord(2L, userRecord2);
    private CreateDinnerRequest createDinnerRequest = generator.createDinnerRequest(location, localDateTime);
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private Dinner dinner = generator.getDinnerFromDinnerRecord(1L, dinnerRecord);
    private AttendeeRecord acceptedAttendeeRecord1 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord pendingAttendeeRecord1 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord1);
    private Attendee acceptedAttendee1 = generator.getAttendeeFromAttendeeRecord(1L, acceptedAttendeeRecord1);
    private FeedbackRecord goodFeedbackRecord = generator.createGoodFeedbackRecord(dinnerRecord, userRecord1, userRecord2);
    private Feedback goodFeedback = generator.getFeedbackFromFeedbackRecord(1L, goodFeedbackRecord);
    private LeaveFeedbackRequest leaveFeedbackRequest = generator.createLeaveFeedbackRequest(user2);

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
        Mockito.when(toApiCompatible.apply(dinnerRecord))
                .thenReturn(dinner);

        Dinner result = dinnerService.createDinner(user1.getId().toString(), createDinnerRequest);

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
        Mockito.when(toApiCompatible.apply(dinnerRecord))
                .thenReturn(dinner);

        Dinner result = dinnerService.createDinner(user1.getId().toString(), createDinnerRequest);

        //then
        assertEquals(dinner.getTitle(), result.getTitle());
        assertEquals(dinner.getCreator(), result.getCreator());
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
        Mockito.when(toApiCompatible.apply(acceptedAttendeeRecord1))
                .thenReturn(acceptedAttendee1);

        Attendee result = dinnerService.joinDinner(principal.getName(), dinnerRecord.getId());

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
        Mockito.when(toApiCompatible.apply(acceptedAttendeeRecord1))
                .thenReturn(acceptedAttendee1);

        Attendee result = dinnerService.joinDinner(principal.getName(), dinnerRecord.getId());

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
        Mockito.when(toApiCompatible.apply(acceptedAttendeeRecord1))
                .thenReturn(acceptedAttendee1);

        Attendee result = dinnerService.joinDinner(principal.getName(), dinnerRecord.getId());

        //then
        assertTrue(result.isAccepted());
    }


    @Test
    public void should_be_able_to_leave_feedback() {
        //given
        userRecord1.setId(1L);
        userRecord2.setId(2L);

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
        Mockito.when(toApiCompatible.apply(goodFeedbackRecord))
                .thenReturn(goodFeedback);

        Feedback result = dinnerService.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);

        //then
        assertEquals(result, goodFeedback);
    }


    @Test
    public void should_not_be_able_to_leave_feedback_for_yourself() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);

        //then
        Executable executable = () -> dinnerService.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);
        assertThrows(IllegalArgumentException.class, executable, "Provider is same as receiver");
    }

    @Test
    public void should_not_be_able_to_leave_feedback_if_did_not_attend_same_dinner() {
        //given
        userRecord1.setId(1L);
        userRecord2.setId(2L);

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
                return count++ == 1;
            }
        });

        //then
        Executable executable = () -> dinnerService.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);
        assertThrows(IllegalArgumentException.class, executable, "No such common dinner for users");
    }

    @Test
    public void should_not_be_able_to_leave_duplicate_feedback() {
        //given
        userRecord1.setId(1L);
        userRecord2.setId(2L);

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
        Mockito.when(dinnerRecordRepository.findById(any()))
                .thenReturn(Optional.of(dinnerRecord));
        Mockito.when(feedbackRecordRepository.isFeedbackPresent(any(), any(), any()))
                .thenReturn(true);
        Mockito.when(feedbackRecordRepository.getFeedback(any(), any(), any()))
                .thenReturn(goodFeedbackRecord);
        Mockito.when(toApiCompatible.apply(goodFeedbackRecord))
                .thenReturn(goodFeedback);

        Feedback result = dinnerService.leaveFeedback(user1.getEmail(), dinnerRecord.getId(), leaveFeedbackRequest);

        //then
        assertEquals(result, goodFeedback);
    }

    @Test
    public void should_return_all_dinner_attendees_with_status() {
        //given
        List<AttendeeRecord> attendeeRecordList = new ArrayList<>();
        attendeeRecordList.add(acceptedAttendeeRecord1);
        dinnerRecord.setId(1L);
        boolean status = true;

        //when
        Mockito.when(attendeeRecordRepository.findDinnerAttendees(anyLong(), anyBoolean()))
                .thenReturn(attendeeRecordList);
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));
        Mockito.when(toApiCompatible.apply(userRecord1))
                .thenReturn(user1);

        List<User> result = dinnerService.findDinnerAttendees(dinnerRecord.getId(), status);

        //then
        assertEquals(1, result.size());
        assertEquals(user1, result.get(0));
    }

    @Test
    public void should_return_good_dinners() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(dinnerRecordRepository.getGoodDinners(any()))
                .thenReturn(List.of(dinnerRecord));

        List<Dinner> result = dinnerService.getGoodMatchDinners(userRecord1.getEmail());

        //then
        assertEquals(1, result.size());
    }

    @Test
    public void should_create_dinner_record_from_create_dinner_request() {
        //when
        Mockito.when(userRecordRepository.findById(any()))
                .thenReturn(Optional.of(userRecord1));

        DinnerRecord result = dinnerService.createDinnerRecordFromRequest(userRecord1.getId(), createDinnerRequest);

        //then
        assertEquals(result.getTitle(), dinnerRecord.getTitle());
        assertEquals(result.getCreator().getEmail(), dinnerRecord.getCreator().getEmail());
        assertEquals(result.getMaxGuests(), dinnerRecord.getMaxGuests());
        assertEquals(result.getDescription(), dinnerRecord.getDescription());
        assertEquals(result.getLocation(), dinnerRecord.getLocation());
        assertEquals(result.getDateTime(), dinnerRecord.getDateTime());
    }


}