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
class DinnerRecordRepositoryTest extends Assertions {

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
    private UserRecord userRecord = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord, location, localDateTime);
    private FeedbackRecord goodFeedbackRecord1to2 = generator.createGoodFeedbackRecord(dinnerRecord, userRecord, userRecord2);
    private FeedbackRecord badFeedbackRecord1to2 = generator.createBadFeedbackRecord(dinnerRecord, userRecord, userRecord2);

    @Test
    void should_return_true_when_match_found() {
        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(this.dinnerRecord);

        //when
        boolean result = dinnerRecordRepository.isDinnerPresent(
                userRecord.getId(),
                dinnerRecord.getTitle(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertTrue(result);
    }

    @Test
    void should_return_false_when_no_match_found() {
        //when
        boolean result = dinnerRecordRepository.isDinnerPresent(
                userRecord.getId(),
                dinnerRecord.getTitle(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertFalse(result);
    }


    @Test
    void should_return_dinner_match() {
        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(this.dinnerRecord);

        //when
        DinnerRecord result = dinnerRecordRepository.getDinner(
                userRecord.getId(),
                dinnerRecord.getTitle(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertEquals(dinnerRecord, result);
    }


    @Test
    void should_return_good_dinners() {
        //given
        userRecord = userRecordRepository.save(userRecord);
        userRecord2 = userRecordRepository.save(userRecord2);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        goodFeedbackRecord1to2 = feedbackRecordRepository.save(goodFeedbackRecord1to2);

        //when
        List<DinnerRecord> result = dinnerRecordRepository.getGoodDinners(userRecord.getId());

        //then
        assertEquals(1, result.size());
        assertEquals(dinnerRecord, result.get(0));
    }

}