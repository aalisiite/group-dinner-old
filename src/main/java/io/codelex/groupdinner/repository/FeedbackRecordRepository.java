package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.FeedbackRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRecordRepository extends JpaRepository<FeedbackRecord, Long> {

    @Query("select feedback from FeedbackRecord feedback where"
            + " feedback.provider.id = :providerId" +
            " and feedback.receiver.id = :receiverId")
    List<FeedbackRecord> getFeedbackRecords(
            @Param("providerId") Long providerId,
            @Param("receiverId") Long receiverId);

    @Query("select count(feedback) > 0 from FeedbackRecord feedback where"
            + " feedback.provider.id = :providerId"
            + " and feedback.receiver.id = :receiverId")
    boolean isFeedbackPresent(
            @Param("providerId") Long provider,
            @Param("receiverId") Long receiver
    );
}
