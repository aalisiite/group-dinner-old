package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendeeRecordRepository extends JpaRepository<AttendeeRecord, Long> {


    @Query("select attendee from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinner_id "
            + " and attendee.isAccepted = :isAccepted ")
    List<AttendeeRecord> findDinnerAttendees(@Param("dinner_id") Long id,
                                             @Param("isAccepted") boolean isAccepted);
    
    @Query("select attendee.isAccepted from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinner_id "
            + " and attendee.user.id = :user_id ")
    boolean getAttendeeIsAccepted (
            @Param("dinner_id") Long dinner_id,
            @Param("user_id") Long user_id
    );
}
