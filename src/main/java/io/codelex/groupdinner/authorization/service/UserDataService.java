package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.api.UserData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDataService {

    private List<UserData> registrations = new ArrayList<>();

    public UserDataService() {
    }

    public Optional<UserData> addUser(UserData userData) {
        if (!isUserDataMatching(userData)) {
            UserData lowerCaseData = new UserData(
                    userData.getEmail().toLowerCase(),
                    userData.getPassword()
            );
            registrations.add(lowerCaseData);
            return Optional.of(lowerCaseData);
        }
        return Optional.empty();
    }

    private boolean isUserDataMatching(UserData data) {
        UserData lowerCaseData = new UserData(
                data.getEmail().toLowerCase(),
                data.getPassword()
        );
        return registrations.contains(lowerCaseData);
    }

    public Optional<UserData> findUser(String email, String password) {
        UserData userData = new UserData(
                email,
                password
        );
        for (UserData user: registrations) {
            if (isUserDataMatching(userData)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
