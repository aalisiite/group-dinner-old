package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserDataService;
import io.codelex.groupdinner.api.UserData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
