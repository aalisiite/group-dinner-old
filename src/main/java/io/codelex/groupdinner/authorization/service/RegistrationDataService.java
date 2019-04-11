package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.api.UserData;

import java.util.ArrayList;
import java.util.List;

public class RegistrationDataService {

    private List<UserData> registrations = new ArrayList<>();

    public RegistrationDataService() {
    }

    public UserData addUser(UserData userData) {
        registrations.add(userData);
        return userData;
    }
}
