package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;

public interface UserModule {

    Dinner createDinner(CreateDinnerRequest request);

    Attendee joinDinner(JoinDinnerRequest request);
}
