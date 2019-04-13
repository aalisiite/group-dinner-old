package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserDataService;
import io.codelex.groupdinner.api.UserAuthorization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositoryUserDataService implements UserDataService {
    
    @Override
    public Optional<UserAuthorization> addUser(UserAuthorization userAuthorization) {
        return Optional.empty();
    }

    @Override
    public Optional<UserAuthorization> findUser(String email, String password) {
        return Optional.empty();
    }
}
