package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.model.DinnerRecord;

public interface UserModule {

    DinnerRecord createDinner(CreateDinnerRequest request);

    Boolean joinDinner(JoinDinnerRequest request);
}
