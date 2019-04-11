package io.codelex.groupdinner.inMemory.service;

import io.codelex.groupdinner.api.Attendee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttendeeService {

    private List<Attendee> attendees = new ArrayList<>();

    public AttendeeService() {
    }

    public Attendee addAttendee(Attendee attendee) {
        attendees.add(attendee);
        return attendee;
    }


}
