package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendeeRecordRepository extends JpaRepository<AttendeeRecord, Long> {


    @Query("select attendee from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinnerId "
            + " and attendee.accepted = :accepted ")
    List<AttendeeRecord> findDinnerAttendees(@Param("dinnerId") Long id, 
                                             @Param("accepted") boolean accepted);

    @Query("select attendee.isAccepted from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinnerId "
            + " and attendee.user.id = :userId ")
    boolean getAttendeeIsAccepted(
            @Param("dinnerId") Long dinnerId,
            @Param("userId") Long userId
    );
}
