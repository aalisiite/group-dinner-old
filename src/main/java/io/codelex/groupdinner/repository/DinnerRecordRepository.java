package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface DinnerRecordRepository extends JpaRepository<DinnerRecord, Long> {


    @Query("select count(dinner) > 0 from DinnerRecord dinner where"
            + " dinner.creator.id = :creator"
            + " and dinner.title = :title"
            + " and dinner.maxGuests = :maxGuests"
            + " and dinner.description = :description"
            + " and dinner.location = :location"
            + " and dinner.dateTime = :dateTime")
    boolean isDinnerPresent(
            @Param("creator") Long userId,
            @Param("title") String title,
            @Param("maxGuests") Integer maxGuests,
            @Param("description") String description,
            @Param("location") String location,
            @Param("dateTime") LocalDateTime dateTime
    );

    @Query("select dinner from DinnerRecord dinner where"
            + " dinner.creator.id = :creator"
            + " and dinner.title = :title"
            + " and dinner.maxGuests = :maxGuests"
            + " and dinner.description = :description"
            + " and dinner.location = :location"
            + " and dinner.dateTime = :dateTime")
    DinnerRecord getDinner(
            @Param("creator") Long userId,
            @Param("title") String title,
            @Param("maxGuests") Integer maxGuests,
            @Param("description") String description,
            @Param("location") String location,
            @Param("dateTime") LocalDateTime dateTime
    );
}