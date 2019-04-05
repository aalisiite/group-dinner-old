package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.model.AttendeeRecord;

import java.util.function.Function;

public class MapAttendeeRecordToAttendee implements Function<AttendeeRecord, Attendee> {
    private MapDinnerRecordToDinner toDinner = new MapDinnerRecordToDinner();

    @Override
    public Attendee apply(AttendeeRecord attendeeRecord) {
        User user = new User(attendeeRecord.getUser().getId(), attendeeRecord.getDinner().getCreator().getFirstName(), attendeeRecord.getDinner().getCreator().getLastName(), attendeeRecord.getDinner().getCreator().getEmail());
        return new Attendee(
                attendeeRecord.getId(),
                toDinner.apply(attendeeRecord.getDinner()),
                user,
                attendeeRecord.getStatus());
    }
}
