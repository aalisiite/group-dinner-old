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


    @Query("select count(attendee) from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinnerId")
    Integer countDinnerAttendees(@Param("dinnerId") Long id);

    @Query("select attendee.accepted from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinnerId "
            + " and attendee.user.id = :userId ")
    boolean getAttendeeStatus(
            @Param("dinnerId") Long dinnerId,
            @Param("userId") Long userId
    );

    @Query("select count(attendee) > 0 from AttendeeRecord attendee"
            + " where attendee.dinner.id = :dinnerId"
            + " and attendee.user.id = :userId")
    Boolean userJoinedDinner(@Param("dinnerId") Long dinnerId,
                               @Param("userId") Long userId);


    
    
}
