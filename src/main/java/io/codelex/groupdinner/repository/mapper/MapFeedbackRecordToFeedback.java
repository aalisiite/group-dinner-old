package io.codelex.groupdinner.repository.mapper;

import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.repository.model.FeedbackRecord;

import java.util.function.Function;

public class MapFeedbackRecordToFeedback implements Function<FeedbackRecord, Feedback> {
    private MapUserRecordToUser toUser = new MapUserRecordToUser();

    @Override
    public Feedback apply(FeedbackRecord feedbackRecord) {
        return new Feedback(
                feedbackRecord.getId(),
                toUser.apply(feedbackRecord.getProvider()),
                toUser.apply(feedbackRecord.getReceiver()),
                feedbackRecord.isRating()
        );
    }
}
