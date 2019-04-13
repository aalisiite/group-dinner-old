package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.api.UserData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegistrationDataService {

    private List<UserData> registrations = new ArrayList<>();

    public RegistrationDataService() {
    }

    public UserData addUser(UserData userData) {
        registrations.add(userData);
        return userData;
    }

    private boolean isUserDataMatching(UserData data, String search) {
        if (search != null && search.length() > 0) {
            if (registrations.contains(data)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public UserData findUser(String email, String password) {
        return registrations.stream()
                .filter(user -> isUserDataMatching(user, email) && isUserDataMatching(user, password))
                .findFirst()
                .orElse(null);
    }
}
