package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.FeedbackRecord;
import io.codelex.groupdinner.repository.model.UserRecord;

public class MapDBRecordToApiCompatible {

    public User apply(UserRecord userRecord) {
        return new User(
                userRecord.getId(),
                userRecord.getFullName(),
                userRecord.getEmail()
        );
    }

    public Attendee apply(AttendeeRecord attendeeRecord) {
        return new Attendee(
                attendeeRecord.getId(),
                apply(attendeeRecord.getDinner()),
                apply(attendeeRecord.getUser()),
                attendeeRecord.isAccepted()
        );
    }

    public Dinner apply(DinnerRecord dinnerRecord) {
        return new Dinner(
                dinnerRecord.getId(),
                dinnerRecord.getTitle(),
                apply(dinnerRecord.getCreator()),
                dinnerRecord.getMaxGuests(),
                dinnerRecord.getDescription(),
                dinnerRecord.getLocation(),
                dinnerRecord.getDateTime()
        );
    }

    public Feedback apply(FeedbackRecord feedbackRecord) {
        return new Feedback(
                feedbackRecord.getId(),
                apply(feedbackRecord.getDinner()),
                apply(feedbackRecord.getProvider()),
                apply(feedbackRecord.getReceiver()),
                feedbackRecord.isRating()
        );
    }

}
