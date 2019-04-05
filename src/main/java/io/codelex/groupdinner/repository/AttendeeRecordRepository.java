package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendeeRecordRepository extends JpaRepository<AttendeeRecord, Long> {


    @Query("select attendee from AttendeeRecord attendee"
            + " where attendee.dinner = :id "
            + " and attendee.status = :status ")
    List<AttendeeRecord> findDinnerAttendees(@Param("dinner") Long id,
                                             @Param("status") boolean status);
}
