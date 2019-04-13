package io.codelex.groupdinner;

import io.codelex.groupdinner.api.AuthRequest;

import java.util.Optional;

public interface UserAuthorizationService {

    Optional<AuthRequest> addUser(AuthRequest authRequest);

    Optional<AuthRequest> findUser(String email, String password);
}
