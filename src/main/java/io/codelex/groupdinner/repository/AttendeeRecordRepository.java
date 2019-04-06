package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AttendeeRecordRepository extends JpaRepository<AttendeeRecord, Long> {


    @Query("select attendee from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinner "
            + " and attendee.isAccepted = :isAccepted ")
    List<AttendeeRecord> findDinnerAttendees(@Param("dinner") Long id,
                                             @Param("isAccepted") boolean isAccepted);
    
    @Query("select attendee.isAccepted from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinner "
            + " and attendee.user.id = :user ")
    boolean getAttendeeIsAccepted (
            @Param("dinner") Long dinner_id,
            @Param("user") Long user_id
    );
}
