package io.codelex.groupdinner.inmemory;


import io.codelex.groupdinner.inmemory.service.AttendeeService;
import io.codelex.groupdinner.inmemory.service.DinnerService;
import io.codelex.groupdinner.inmemory.service.UsersService;
import io.codelex.groupdinner.UserService;
import io.codelex.groupdinner.api.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserService implements UserService {
    private AttendeeService attendeeService;
    private DinnerService dinnerService;
    private UsersService usersService;
    private AtomicLong attendeeSequence = new AtomicLong(1L);
    private AtomicLong dinnerSequence = new AtomicLong(1L);
    private AtomicLong userSequence = new AtomicLong(1L);

    public InMemoryUserService(AttendeeService attendeeService, DinnerService dinnerService, UsersService usersService) {
        this.attendeeService = attendeeService;
        this.dinnerService = dinnerService;
        this.usersService = usersService;
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
    public User registerUser(String firstName, String lastName, String email, String password) {
        Optional<User> user = usersService.getUserByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            User newUser = new User(
                    userSequence.getAndIncrement(),
                    firstName,
                    lastName,
                    email
            );
            usersService.addUser(newUser);
        }
        return null;
    }

    @Override
    public User authenticateUser(String email, String password) {
        Optional<User> user = usersService.getUserByEmail(email.toLowerCase());
        return user.orElse(null);
    }

    @Override
    public Attendee joinDinner(String userId, Long dinnerId) {
        Optional<Dinner> dinner = dinnerService.getDinner(dinnerId);
        Long userIdLong = Long.parseLong(userId);
        Optional<User> user = usersService.getUser(userIdLong);
        if (dinner.isPresent() && user.isPresent()) {
            boolean isAccepted = dinner.get().shouldAcceptRequest();
            Attendee attendee = new Attendee(
                    attendeeSequence.getAndIncrement(),
                    dinner.get(),
                    user.get(),
                    isAccepted
            );
            attendeeService.addAttendee(attendee);
            dinner.get().incrementCurrentGuests();
            return attendee;
        }
        return null;
    }

    @Override //todo
    public Feedback leaveFeedback(String provider, Long dinnerId, LeaveFeedbackRequest request) {
        return null;
    }
}
