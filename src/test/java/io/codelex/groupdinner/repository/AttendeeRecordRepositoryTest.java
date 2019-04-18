package io.codelex.groupdinner.repository;


import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
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
public class AttendeeRecordRepositoryTest extends Assertions {

    private final TestVariableGenerator generator = new TestVariableGenerator();
    @Autowired
    DinnerRecordRepository dinnerRecordRepository;
    @Autowired
    AttendeeRecordRepository attendeeRecordRepository;
    @Autowired
    UserRecordRepository userRecordRepository;
    private LocalDateTime localDateTime = generator.createDateTime();
    private String location = generator.createLocation();
    private UserRecord userRecord1 = generator.createUserRecord1();
    private UserRecord userRecord2 = generator.createUserRecord2();
    private DinnerRecord dinnerRecord = generator.createDinnerRecord(userRecord1, location, localDateTime);
    private AttendeeRecord acceptedAttendeeRecord1 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord1);
    private AttendeeRecord acceptedAttendeeRecord2 = generator.createAcceptedAttendeeRecord(dinnerRecord, userRecord2);
    private AttendeeRecord pendingAttendeeRecord2 = generator.createPendingAttendeeRecord(dinnerRecord, userRecord2);

    @Test
    public void should_return_all_accepted_attendees_to_dinner() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        userRecord2 = userRecordRepository.save(userRecord2);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        acceptedAttendeeRecord1 = attendeeRecordRepository.save(acceptedAttendeeRecord1);
        acceptedAttendeeRecord2 = attendeeRecordRepository.save(acceptedAttendeeRecord2);

        //when
        List<AttendeeRecord> result = attendeeRecordRepository.findDinnerAttendees(dinnerRecord.getId(), true);

        //then
        assertEquals(2, result.size());
    }

    @Test
    public void should_return_all_pending_attendees_to_dinner() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        userRecord2 = userRecordRepository.save(userRecord2);
        acceptedAttendeeRecord1 = attendeeRecordRepository.save(acceptedAttendeeRecord1);
        pendingAttendeeRecord2 = attendeeRecordRepository.save(pendingAttendeeRecord2);

        //when
        List<AttendeeRecord> result = attendeeRecordRepository.findDinnerAttendees(dinnerRecord.getId(), false);

        //then
        assertEquals(1, result.size());
    }


    @Test
    public void should_return_count_of_attendees_to_dinner() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        userRecord2 = userRecordRepository.save(userRecord2);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        acceptedAttendeeRecord1 = attendeeRecordRepository.save(acceptedAttendeeRecord1);
        pendingAttendeeRecord2 = attendeeRecordRepository.save(pendingAttendeeRecord2);

        //when
        Integer result = attendeeRecordRepository.countDinnerAttendees(dinnerRecord.getId());

        //then
        assertEquals(2, result);
    }

    @Test
    void should_return_attendee_status() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        acceptedAttendeeRecord1 = attendeeRecordRepository.save(acceptedAttendeeRecord1);

        //when
        boolean result = attendeeRecordRepository.getAttendeeStatus(dinnerRecord.getId(), userRecord1.getId());

        //then
        assertTrue(result);
    }

    @Test
    void should_return_true_if_user_has_joined_dinner() {
        //given
        userRecord1 = userRecordRepository.save(userRecord1);
        dinnerRecord = dinnerRecordRepository.save(dinnerRecord);
        acceptedAttendeeRecord1 = attendeeRecordRepository.save(acceptedAttendeeRecord1);

        //when
        boolean result = attendeeRecordRepository.userJoinedDinner(dinnerRecord.getId(), userRecord1.getId());

        //then
        assertTrue(result);
    }

    @Test
    void should_return_false_if_user_has_not_joined_dinner() {
        //when
        boolean result = attendeeRecordRepository.userJoinedDinner(dinnerRecord.getId(), userRecord1.getId());

        //then
        assertFalse(result);
    }

}