package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.model.Dinner;

public interface UserModule {

    Dinner createDinner(CreateDinnerRequest request);

    Boolean joinDinner(JoinDinnerRequest request);
}
