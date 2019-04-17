package io.codelex.groupdinner.repository;

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

    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private UserRecord userRecord = createUserRecord();
    private String location = createLocation();
    private DinnerRecord dinnerRecord = createDinnerRecord();

    @BeforeEach
    void setUp() {
        attendeeRecordRepository.deleteAll();
        dinnerRecordRepository.deleteAll();
        userRecordRepository.deleteAll();
        feedbackRecordRepository.deleteAll();
    }


    @Test
    void is_dinner_present_should_return_true_when_match_found() {

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
    void is_dinner_present_should_return_false_when_no_match_found() {

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

    private UserRecord createUserRecord() {
        return new UserRecord(
                "Janis",
                "Berzins",
                "berzins@gmai.com",
                "password"
        );
    }

    private String createLocation() {
        return "Jurmalas Gatve 76";
    }

}