package io.codelex.groupdinner.authorization.repository;

import io.codelex.groupdinner.authorization.UserDataService;
import io.codelex.groupdinner.authorization.api.UserData;

import java.util.Optional;

public class RepositoryUserDataService implements UserDataService {
    @Override
    public Optional<UserData> addUser(UserData userData) {
        return Optional.empty();
    }

    @Override
    public Optional<UserData> findUser(String email, String password) {
        return Optional.empty();
    }
}
