package io.codelex.groupdinner.InMemory.service;

import io.codelex.groupdinner.api.Attendee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttendeeService {

    List<Attendee> attendees;//todo

    public AttendeeService(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public Attendee addAttendee(Attendee attendee) {
        attendees.add(attendee);
        return attendee;
    }


}
