package io.codelex.groupdinner.authorization;

import io.codelex.groupdinner.authorization.api.CreateUserRegRequest;
import io.codelex.groupdinner.authorization.api.UserData;
import io.codelex.groupdinner.authorization.service.RegistrationDataService;
import org.springframework.stereotype.Component;

@Component
public class RegistrationService {

    private RegistrationDataService service;

    public UserData saveUser(CreateUserRegRequest request) {
        UserData userData = new UserData(
                request.getEmail(),
                request.getPassword()
        );
        service.addUser(userData);
        return userData;
    }
}
