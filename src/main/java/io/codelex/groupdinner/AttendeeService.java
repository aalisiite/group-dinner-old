package io.codelex.groupdinner;

import io.codelex.groupdinner.repository.model.AttendeeRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttendeeService {

    List<AttendeeRecord> attendees;

    AttendeeService(List<AttendeeRecord> attendees) {
        this.attendees = attendees;
    }

    public AttendeeRecord addAttendee(AttendeeRecord attendee) {
        attendees.add(attendee);
        return attendee;
    }


}
