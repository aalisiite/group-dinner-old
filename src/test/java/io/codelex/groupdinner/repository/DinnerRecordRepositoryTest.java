package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DinnerRecordRepositoryTest extends Assertions {

    @Autowired
    DinnerRecordRepository dinnerRecordRepository;

    @Autowired
    AttendeeRecordRepository attendeeRecordRepository;
    
    @Autowired
    UserRecordRepository userRecordRepository;
    
    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private UserRecord userRecord = createUserRecord();
    private String location = createLocation();
    private DinnerRecord dinnerRecord = createDinnerRecord();
    
    @BeforeEach
    void setUp() {
        attendeeRecordRepository.deleteAll();
        dinnerRecordRepository.deleteAll();
        userRecordRepository.deleteAll();
    }
    

    @Test
    void is_dinner_present_should_return_true_when_match_found() {

        //given
        userRecordRepository.save(userRecord);
        DinnerRecord dinnerRecord = dinnerRecordRepository.save(this.dinnerRecord);
        
        //when
        boolean isDinnerPresent = dinnerRecordRepository.isDinnerPresent(
                dinnerRecord.getTitle(),
                dinnerRecord.getCreator().getId(),
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
                dinnerRecord.getTitle(),
                dinnerRecord.getCreator().getId(),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );

        //then
        assertFalse(isDinnerPresent);
    }
    
    @Test
    void should_increment_current_guests_by_one () {
        //given
        userRecordRepository.save(userRecord);
        DinnerRecord dinnerRecord = dinnerRecordRepository.save(this.dinnerRecord);
        
        Integer initialGuests = dinnerRecord.getCurrentGuests();
        
        //when
        dinnerRecordRepository.incrementCurrentGuests(dinnerRecord.getId());
        dinnerRecordRepository.incrementCurrentGuests(dinnerRecord.getId());
        
        //then
        Optional<DinnerRecord> result = dinnerRecordRepository.findById(dinnerRecord.getId());
        assertEquals(initialGuests+1, result.get().getCurrentGuests());
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
                "berzins@gmai.com"
        );
    }
    
    private String createLocation() {
        return "Jurmalas Gatve 76";
    }


}