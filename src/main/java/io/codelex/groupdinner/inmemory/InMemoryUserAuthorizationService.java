package io.codelex.groupdinner.inmemory;

import io.codelex.groupdinner.UserAuthorizationService;
import io.codelex.groupdinner.api.UserAuthorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserAuthorizationService implements UserAuthorizationService {

    private List<UserAuthorization> registrations = new ArrayList<>();

    public InMemoryUserAuthorizationService() {
    }

    @Override
    public Optional<UserAuthorization> addUser(UserAuthorization userAuthorization) {
        if (!isUserDataMatching(userAuthorization)) {
            UserAuthorization lowerCaseData = new UserAuthorization(
                    userAuthorization.getEmail().toLowerCase(),
                    userAuthorization.getPassword()
            );
            registrations.add(lowerCaseData);
            return Optional.of(lowerCaseData);
        }
        return Optional.empty();
    }

    private boolean isUserDataMatching(UserAuthorization data) {
        UserAuthorization lowerCaseData = new UserAuthorization(
                data.getEmail().toLowerCase(),
                data.getPassword()
        );
        return registrations.contains(lowerCaseData);
    }

    @Override
    public Optional<UserAuthorization> findUser(String email, String password) {
        UserAuthorization userAuthorization = new UserAuthorization(
                email,
                password
        );
        for (UserAuthorization user : registrations) {
            if (isUserDataMatching(userAuthorization)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
