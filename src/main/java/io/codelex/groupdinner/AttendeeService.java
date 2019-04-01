package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class AttendeeService {

    List<Attendee> attendees;
    AttendeeService(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    Attendee addAttendee(Attendee attendee) {
        attendees.add(attendee);
        return attendee;
    }


}
