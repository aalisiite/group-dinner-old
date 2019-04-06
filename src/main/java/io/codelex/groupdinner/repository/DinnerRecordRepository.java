package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DinnerRecordRepository extends JpaRepository<DinnerRecord, Long> {


    @Query("select count(dinner) > 0 from DinnerRecord dinner where"
            + " dinner.title = :title"
            + " and dinner.creator = :creator"
            + " and dinner.maxGuests = :maxGuests"
            + " and dinner.description = :description"
            + " and dinner.location = :location"
            + " and dinner.dateTime = :dateTime")
    boolean isDinnerPresent(
            @Param("title") String title,
            @Param("creator") Long creator,
            @Param("maxGuests") int maxGuests,
            @Param("description") String description,
            @Param("location") String location,
            @Param("dateTime") LocalDateTime dateTime
    );
    
    @Query("UPDATE DinnerRecord dinner SET dinner.currentGuests = dinner.currentGuests + 1 " +
            "WHERE dinner.id = :id")
    void incrementCurrentGuests(
            @Param("id") Long id
    );
}