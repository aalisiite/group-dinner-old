package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.model.AttendeeRecord;

import java.util.function.Function;

public class MapAttendeeRecordToAttendee implements Function<AttendeeRecord, Attendee> {
    private MapDinnerRecordToDinner toDinner = new MapDinnerRecordToDinner();
    private MapUserRecordToUser toUser = new MapUserRecordToUser();

    @Override
    public Attendee apply(AttendeeRecord attendeeRecord) {
        return new Attendee(
                attendeeRecord.getId(),
                toDinner.apply(attendeeRecord.getDinner()),
                toUser.apply(attendeeRecord.getUser()),
                attendeeRecord.getIsAccepted());
    }
}
