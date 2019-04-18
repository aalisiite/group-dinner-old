package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackRecordRepositoryTest extends Assertions {

    private final TestVariableGenerator generator = new TestVariableGenerator();
    @Autowired
    DinnerRecordRepository dinnerRecordRepository;
    @Autowired
    AttendeeRecordRepository attendeeRecordRepository;
    @Autowired
    UserRecordRepository userRecordRepository;
    @Autowired
    FeedbackRecordRepository feedbackRecordRepository;
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private FeedbackRecord goodFeedbackRecord = generator.createGoodFeedbackRecord(dinnerRecord, userRecord1, userRecord2);

    @Test
    public void should_return_feedback_by_provider_receiver_ids() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        userRecord2 = userRecordRepository.save(userRecord2);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        goodFeedbackRecord = feedbackRecordRepository.save(goodFeedbackRecord);

        //when
        List<FeedbackRecord> result = feedbackRecordRepository.getFeedbacksForUsers(userRecord1.getId(), userRecord2.getId());

        //then
        assertEquals(goodFeedbackRecord, ((List) result).get(0));
        assertTrue(result.get(0).isRating());
    }

    @Test
    public void should_return_true_if_given_feedback_present() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        userRecord2 = userRecordRepository.save(userRecord2);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        goodFeedbackRecord = feedbackRecordRepository.save(goodFeedbackRecord);

        //when
        boolean result = feedbackRecordRepository.isFeedbackPresent(dinnerRecord.getId(), userRecord1.getId(), userRecord2.getId());

        //then
        assertTrue(result);
    }

    @Test
    public void should_return_false_if_given_feedback_not_present() {
        //when
        boolean result = feedbackRecordRepository.isFeedbackPresent(1L, 1L, 2L);

        //then
        assertFalse(result);
    }

}