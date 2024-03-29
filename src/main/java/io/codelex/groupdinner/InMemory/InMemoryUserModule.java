package io.codelex.groupdinner.InMemory;

import io.codelex.groupdinner.InMemory.service.AttendeeService;
import io.codelex.groupdinner.InMemory.service.DinnerService;
import io.codelex.groupdinner.UserModule;
import io.codelex.groupdinner.api.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserModule implements UserModule {
    private User user;
    private AttendeeService attendeeService;
    private DinnerService dinnerService;
    private AtomicLong attendeeSequence = new AtomicLong(1L);
    private Long id;

    public InMemoryUserModule(AttendeeService attendeeService, DinnerService dinnerService) {
        this.attendeeService = attendeeService;
        this.dinnerService = dinnerService;
    }

    @Override
    public Dinner createDinner(CreateDinnerRequest request
    ) {
        Dinner dinner = new Dinner(
                attendeeSequence.getAndIncrement(),
                request.getTitle(),
                request.getCreator(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        );
        dinnerService.addDinner(dinner);
        attendeeService.addAttendee(new Attendee(id, dinner, user, true));
        return dinner;
    }

    @Override
    public Attendee joinDinner(JoinDinnerRequest request) {
        Optional<Dinner> dinner = dinnerService.getDinner(request);
        //???? need to check or not? because can join only existing dinner anyway
        //if (dinner.isPresent()) {
        boolean status = dinner.get().shouldAcceptRequest();
        Attendee attendee = new Attendee(
                id,
                dinner.get(),
                user,
                status
        );
        attendeeService.addAttendee(attendee);
        dinner.get().incrementCurrentGuests();
        return attendee;
        //}
    }
}
