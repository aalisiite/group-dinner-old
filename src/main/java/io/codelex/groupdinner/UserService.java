package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

public interface UserService {

    Dinner createDinner(CreateDinnerRequest request);
    
    User registerUser(String firstName, String lastName, String email, String password);
    
    User authenticateUser(String email, String password);

    Attendee joinDinner(String userId, Long dinnerId);

    Feedback leaveFeedback(String provider, Long dinnerId, LeaveFeedbackRequest request);
}
