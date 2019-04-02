package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

import java.util.Optional;
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
                attendeeSequence.getAndIncrement(),
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


     Boolean joinDinner(JoinDinnerRequest request) {
        Optional<Dinner> dinner = dinnerService.getDinner(request);
        //???? need to check or not? because can join only existing dinner anyway
        //if (dinner.isPresent()) {
            if (dinner.get().shouldAcceptRequest()) {
                attendeeService.addAttendee(
                        new Attendee(
                                dinner.get(),
                                user,
                                true
                        )
                );
                dinner.get().incrementCurrentGuests();
                return true;
            } else {
                attendeeService.addAttendee(
                        new Attendee(
                                dinner.get(),
                                user,
                                false
                        )
                );
                dinner.get().incrementCurrentGuests();
                return false;
            }
        //}
    }
}
