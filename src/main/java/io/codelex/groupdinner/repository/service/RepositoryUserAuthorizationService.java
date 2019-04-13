package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.UserAuthorizationService;
import io.codelex.groupdinner.api.AuthRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositoryUserAuthorizationService implements UserAuthorizationService {
    
    @Override
    public Optional<AuthRequest> addUser(AuthRequest authRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthRequest> findUser(String email, String password) {
        return Optional.empty();
    }
}
