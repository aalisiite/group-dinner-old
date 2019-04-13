package io.codelex.groupdinner.inmemory;

import io.codelex.groupdinner.authorization.UserDataService;
import io.codelex.groupdinner.authorization.api.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserDataService implements UserDataService {

    private List<UserData> registrations = new ArrayList<>();

    public InMemoryUserDataService() {
    }

    @Override
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

    @Override
    public Optional<UserData> findUser(String email, String password) {
        UserData userData = new UserData(
                email,
                password
        );
        for (UserData user : registrations) {
            if (isUserDataMatching(userData)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
