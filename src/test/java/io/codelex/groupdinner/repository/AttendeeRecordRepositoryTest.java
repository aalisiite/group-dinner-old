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
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttendeeRecordRepositoryTest extends Assertions {

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
    private AttendeeRecord attendeeRecord = createAttendeeRecord();

    @BeforeEach
    void setUp() {
        attendeeRecordRepository.deleteAll();
        dinnerRecordRepository.deleteAll();
        userRecordRepository.deleteAll();
    }

    @Test
    public void should_return_all_accepted_attendees_to_given_dinner() {

        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        UserRecord userRecord2 = new UserRecord(
                "Anna",
                "Kalniņa",
                "a.kalnina@gmail.com"
        );
        userRecord2 = userRecordRepository.save(userRecord2);
        AttendeeRecord attendeeRecord2 = new AttendeeRecord(
                dinnerRecord,
                userRecord2,
                true
        );
        attendeeRecord = attendeeRecordRepository.save(attendeeRecord);
        attendeeRecordRepository.save(attendeeRecord2);

        //when
        List<AttendeeRecord> attendeeRecords = attendeeRecordRepository.findDinnerAttendees(dinnerRecord.getId(), true);

        //then
        assertEquals(2, attendeeRecords.size());
    }

    @Test
    public void should_return_all_pending_attendees_to_given_dinner() {

        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        UserRecord userRecord2 = new UserRecord(
                "Anna",
                "Kalniņa",
                "a.kalnina@gmail.com"
        );
        userRecord2 = userRecordRepository.save(userRecord2);
        AttendeeRecord attendeeRecord = new AttendeeRecord(
                dinnerRecord,
                userRecord2,
                false
        );
        attendeeRecordRepository.save(attendeeRecord);

        //when
        List<AttendeeRecord> attendeeRecords = attendeeRecordRepository.findDinnerAttendees(dinnerRecord.getId(), true);

        //then
        assertEquals(1, attendeeRecords.size());
    }

    @Test
    void should_return_attendee_status() {
        //given
        userRecord = userRecordRepository.save(userRecord);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        attendeeRecord = attendeeRecordRepository.save(attendeeRecord);

        //when
        boolean result = attendeeRecordRepository.getAttendeeIsAccepted(dinnerRecord.getId(), userRecord.getId());

        //then
        assertTrue(result);
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
                "berzins@gmai.com"
        );
        return userRecord;
    }

    private AttendeeRecord createAttendeeRecord() {
        AttendeeRecord attendeeRecord = new AttendeeRecord(
                dinnerRecord,
                userRecord,
                true
        );
        return attendeeRecord;
    }

    private String createLocation() {
        return "Jurmalas Gatve 76";
    }
}