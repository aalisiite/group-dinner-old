package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

public interface UserService {

    Dinner createDinner(CreateDinnerRequest request);

    Attendee joinDinner(String userId, Long dinnerId);

    Feedback leaveFeedback(String provider, Long dinnerId, LeaveFeedbackRequest request);
}
