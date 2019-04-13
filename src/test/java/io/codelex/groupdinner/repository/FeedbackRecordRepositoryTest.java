package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackRecordRepositoryTest extends Assertions {

    @Autowired
    DinnerRecordRepository dinnerRecordRepository;

    @Autowired
    AttendeeRecordRepository attendeeRecordRepository;

    @Autowired
    UserRecordRepository userRecordRepository;

    @Autowired
    FeedbackRecordRepository feedbackRecordRepository;

    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private UserRecord userRecord = createUserRecord();
    private String location = createLocation();
    private DinnerRecord dinnerRecord = createDinnerRecord();

    //todo 
    //create separate class in tests that returns list of example UserRecords, DinnerRecords, AttendeeRecords, FeedbackRecords
    
    @BeforeEach
    void setUp() {
        attendeeRecordRepository.deleteAll();
        dinnerRecordRepository.deleteAll();
        userRecordRepository.deleteAll();
        feedbackRecordRepository.deleteAll();
    }
    
    @Test
    public void should_return_feedback_by_provider_receiver_ids () {
        //given
        userRecord = userRecordRepository.save(userRecord);
        UserRecord userRecord2 = new UserRecord(
                "Anna",
                "Kalniņa",
                "a.kalnina@gmail.com",
                "password"
        );
        userRecord2 = userRecordRepository.save(userRecord2);
        FeedbackRecord feedbackRecord = new FeedbackRecord(
                userRecord,
                userRecord2,
                true
        );
        feedbackRecordRepository.save(feedbackRecord);
        
        //when
        FeedbackRecord result = feedbackRecordRepository.getFeedbackRecord(userRecord.getId(), userRecord2.getId());
        
        //then
        assertEquals(feedbackRecord, result);
        assertTrue(result.isRating());
    }

    @Test
    public void should_return_true_if_given_feedback_present () {
        //given
        userRecord = userRecordRepository.save(userRecord);
        UserRecord userRecord2 = new UserRecord(
                "Anna",
                "Kalniņa",
                "a.kalnina@gmail.com",
                "password"
        );
        userRecord2 = userRecordRepository.save(userRecord2);
        FeedbackRecord feedbackRecord = new FeedbackRecord(
                userRecord,
                userRecord2,
                true
        );
        feedbackRecordRepository.save(feedbackRecord);

        //when
        boolean result = feedbackRecordRepository.isFeedbackPresent(userRecord.getId(), userRecord2.getId());

        //then
        assertTrue(result);
    }

    @Test
    public void should_return_false_if_given_feedback_not_present () {
        //when
        boolean result = feedbackRecordRepository.isFeedbackPresent(1L, 2L);

        //then
        assertFalse(result);
    }

    private DinnerRecord createDinnerRecord() {
        DinnerRecord dinnerRecord = new DinnerRecord(
                "This is a title",
                userRecord,
                2,
                "This is a description",
                location,
                localDateTime
        );
        return dinnerRecord;
    }

    private UserRecord createUserRecord() {
        UserRecord userRecord = new UserRecord(
                "Janis",
                "Berzins",
                "berzins@gmai.com",
                "password"
        );
        return userRecord;
    }
    
    private String createLocation() {
        return "Jurmalas Gatve 76";
    }
    
}