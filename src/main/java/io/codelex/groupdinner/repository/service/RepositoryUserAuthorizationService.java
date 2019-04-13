package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserAuthorizationService;
import io.codelex.groupdinner.api.UserAuthorization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositoryUserAuthorizationService implements UserAuthorizationService {
    
    @Override
    public Optional<UserAuthorization> addUser(UserAuthorization userAuthorization) {
        return Optional.empty();
    }

    @Override
    public Optional<UserAuthorization> findUser(String email, String password) {
        return Optional.empty();
    }
}
