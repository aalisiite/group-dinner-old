package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

import java.util.List;

public interface UserService {

    Dinner createDinner(String userEmail, CreateDinnerRequest request);

    User registerUser(RegistrationRequest request);

    User authenticateUser(SignInRequest request);

    Attendee joinDinner(String userEmail, Long dinnerId);

    Feedback leaveFeedback(String providerEmail, Long dinnerId, LeaveFeedbackRequest request);

    Dinner findDinner(Long id);

    List<User> findDinnerAttendees(Long dinnerId, boolean accepted);

    List<Dinner> getGoodMatchDinners(String userEmail);
}
