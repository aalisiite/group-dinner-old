package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

class UserModule {

    private User user;
    private AttendeeService attendeeService;
    private DinnerService dinnerService;
    private AtomicLong attendeeSequence = new AtomicLong(1L);
    
    UserModule(AttendeeService attendeeService, DinnerService dinnerService) {
        this.attendeeService = attendeeService;
        this.dinnerService = dinnerService;
    }

    Dinner createDinner(CreateDinnerRequest request
                        ) {
        Dinner dinner = new Dinner(
                attendeeSequence.incrementAndGet(),
                request.getTitle(),
                request.getCreator(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        );
        dinnerService.addDinner(dinner);
        attendeeService.addAttendee(new Attendee(dinner, user, true));
        return dinner;
    }


    void joinDinner(User user, Dinner dinner) {
        if (dinner.shouldAcceptRequest()) {
            attendeeService.addAttendee(
                    new Attendee(
                            dinner,
                            user,
                            true
                    )
            );
            dinner.incrementCurrentGuests();
        } else {
            attendeeService.addAttendee(
                    new Attendee(
                            dinner,
                            user,
                            false
                    )
            );
            dinner.incrementCurrentGuests();
        }
    }
}
