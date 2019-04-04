package io.codelex.groupdinner.repository;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.repository.model.AttendeeRecord;

import java.util.function.Function;

public class MapAttendeeRecordToAttendee implements Function<AttendeeRecord, Attendee> {
    @Override
    public Attendee apply(AttendeeRecord attendeeRecord) {
        return new Attendee(
                attendeeRecord.getId(),
                attendeeRecord.getDinner(),
                attendeeRecord.getUser(),
                attendeeRecord.getStatus()
        );
    }
}
