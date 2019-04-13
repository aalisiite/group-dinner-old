package io.codelex.groupdinner;

import io.codelex.groupdinner.api.UserAuthorization;

import java.util.Optional;

public interface UserAuthorizationService {

    Optional<UserAuthorization> addUser(UserAuthorization userAuthorization);

    Optional<UserAuthorization> findUser(String email, String password);
}
