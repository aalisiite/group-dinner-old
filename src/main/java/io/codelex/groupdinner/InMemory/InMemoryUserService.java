package io.codelex.groupdinner.InMemory;


import io.codelex.groupdinner.InMemory.service.AttendeeService;
import io.codelex.groupdinner.InMemory.service.DinnerService;
import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserService implements UserService {
    private AttendeeService attendeeService;
    private DinnerService dinnerService;
    private AtomicLong attendeeSequence = new AtomicLong(1L);
    private AtomicLong dinnerSequence = new AtomicLong(1L);

    public InMemoryUserService(AttendeeService attendeeService, DinnerService dinnerService) {
        this.attendeeService = attendeeService;
        this.dinnerService = dinnerService;
    }

    @Override
    public Dinner createDinner(CreateDinnerRequest request
    ) {
        Dinner dinner = new Dinner(
                dinnerSequence.getAndIncrement(),
                request.getTitle(),
                request.getCreator(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        );
        dinnerService.addDinner(dinner);
        attendeeService.addAttendee(new Attendee(attendeeSequence.getAndIncrement(), dinner, dinner.getCreator(), true));
        return dinner;
    }

    @Override
    public Attendee joinDinner(JoinDinnerRequest request) {
        Optional<Dinner> dinner = dinnerService.getDinner(request);
        //???? need to check or not? because can join only existing dinner anyway
        //if (dinner.isPresent()) {
        boolean isAccepted = dinner.get().shouldAcceptRequest();
        Attendee attendee = new Attendee(
                attendeeSequence.getAndIncrement(),
                dinner.get(),
                request.getUser(),
                isAccepted
        );
        attendeeService.addAttendee(attendee);
        dinner.get().incrementCurrentGuests();
        return attendee;
        //}
    }
}
