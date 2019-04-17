package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRecordRepository extends JpaRepository<FeedbackRecord, Long> {

    @Query("select feedback from FeedbackRecord feedback where"
            + " feedback.provider.id = :providerId" +
            " and feedback.receiver.id = :receiverId")
    List<FeedbackRecord> getFeedbacksForUsers(
            @Param("providerId") Long providerId,
            @Param("receiverId") Long receiverId);

    @Query("select feedback from FeedbackRecord feedback where"
            + " feedback.dinner.id = :dinnerId" +
            " and feedback.provider.id = :providerId" +
            " and feedback.receiver.id = :receiverId")
    FeedbackRecord getFeedback(
            @Param("dinnerId") Long dinnerId,
            @Param("providerId") Long providerId,
            @Param("receiverId") Long receiverId);

    @Query("select count(feedback) > 0 from FeedbackRecord feedback where"
            + " feedback.dinner.id = :dinnerId"
            + " and feedback.provider.id = :providerId"
            + " and feedback.receiver.id = :receiverId")
    boolean isFeedbackPresent(
            @Param("dinnerId") Long dinnerId,
            @Param("providerId") Long providerId,
            @Param("receiverId") Long receiverId
    );

    @Query("select feedback.receiver from FeedbackRecord feedback where"
            + " feedback.provider.id = :providerId"
            + " and feedback.rating = false")
    List<UserRecord> getBadFeedbackUsers(
            @Param("providerId") Long providerId);
}
