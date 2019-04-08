package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;

public interface UserService {

    Dinner createDinner(CreateDinnerRequest request);

    Attendee joinDinner(String userId, Long dinnerId);
}
