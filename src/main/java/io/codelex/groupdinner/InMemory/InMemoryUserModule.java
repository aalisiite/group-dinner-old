package io.codelex.groupdinner.InMemory;


import io.codelex.groupdinner.AttendeeService;
import io.codelex.groupdinner.DinnerService;
import io.codelex.groupdinner.UserModule;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.JoinDinnerRequest;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.repository.model.UserRecord;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserModule implements UserModule {
    private UserRecord user;
    private AttendeeService attendeeService;
    private DinnerService dinnerService;
    private AtomicLong attendeeSequence = new AtomicLong(1L);

    public InMemoryUserModule(AttendeeService attendeeService, DinnerService dinnerService) {
        this.attendeeService = attendeeService;
        this.dinnerService = dinnerService;
    }

    @Override
    public DinnerRecord createDinner(CreateDinnerRequest request
    ) {
        DinnerRecord dinner = new DinnerRecord(
                attendeeSequence.getAndIncrement(),
                request.getTitle(),
                request.getCreator(),
                request.getMaxGuests(),
                request.getDescription(),
                request.getLocation(),
                request.getDateTime()
        );
        dinnerService.addDinner(dinner);
        attendeeService.addAttendee(new AttendeeRecord(dinner, user, true));
        return dinner;
    }

    @Override
    public Boolean joinDinner(JoinDinnerRequest request) {
        Optional<DinnerRecord> dinner = dinnerService.getDinner(request);
        //???? need to check or not? because can join only existing dinner anyway
        //if (dinner.isPresent()) {
        if (dinner.get().shouldAcceptRequest()) {
            attendeeService.addAttendee(
                    new AttendeeRecord(
                            dinner.get(),
                            user,
                            true
                    )
            );
            dinner.get().incrementCurrentGuests();
            return true;
        } else {
            attendeeService.addAttendee(
                    new AttendeeRecord(
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
