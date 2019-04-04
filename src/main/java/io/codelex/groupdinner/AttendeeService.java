package io.codelex.groupdinner;

import io.codelex.groupdinner.repository.model.Attendee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttendeeService {

    List<Attendee> attendees;

    AttendeeService(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public Attendee addAttendee(Attendee attendee) {
        attendees.add(attendee);
        return attendee;
    }


}
