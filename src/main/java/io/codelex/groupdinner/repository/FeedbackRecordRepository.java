package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.FeedbackRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRecordRepository extends JpaRepository<FeedbackRecord, Long> {

    @Query("select feedback from FeedbackRecord feedback where"
            + " feedback.provider.id = :provider_id" +
            " and feedback.receiver.id = :receiver_id")
    FeedbackRecord getFeedbackRecord(
            @Param("provider_id") Long providerId,
            @Param("receiver_id") Long receiverId);

    @Query("select count(feedback) > 0 from FeedbackRecord feedback where"
            + " feedback.provider.id = :provider_id"
            + " and feedback.receiver.id = :receiver_id")
    boolean isFeedbackPresent(
            @Param("provider_id") Long provider,
            @Param("receiver_id") Long receiver
    );
}
