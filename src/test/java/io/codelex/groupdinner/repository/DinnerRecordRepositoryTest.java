package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DinnerRecordRepositoryTest extends Assertions {

    @Autowired
    DinnerRecordRepository dinnerRecordRepository;

    @Autowired
    AttendeeRecordRepository attendeeRecordRepository;

    @Autowired
    UserRecordRepository userRecordRepository;

    @Autowired
    FeedbackRecordRepository feedbackRecordRepository;
    
    private final TestVariableGenerator generator = new TestVariableGenerator();
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    private UserRecord userRecord = generator.createUserRecord1();
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord,location,localDateTime);

    @Test
    void should_return_true_when_match_found() {

        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(this.dinnerRecord);

        //when
        boolean isDinnerPresent = dinnerRecordRepository.isDinnerPresent(
                userRecord.getId(),
                dinnerRecord.getTitle(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertTrue(isDinnerPresent);
    }

    @Test
    void should_return_false_when_no_match_found() {

        //when
        boolean isDinnerPresent = dinnerRecordRepository.isDinnerPresent(
                userRecord.getId(),
                dinnerRecord.getTitle(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertFalse(isDinnerPresent);
    }

}