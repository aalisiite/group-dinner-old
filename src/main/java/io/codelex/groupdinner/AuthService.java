package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SignInRequest;
import io.codelex.groupdinner.api.User;

public interface AuthService {

    User registerUser(RegistrationRequest request);

    User authenticateUser(SignInRequest request);
}
