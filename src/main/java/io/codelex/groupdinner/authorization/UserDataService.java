package io.codelex.groupdinner.authorization;

import io.codelex.groupdinner.authorization.api.UserData;

import java.util.Optional;

public interface UserDataService {

    Optional<UserData> addUser(UserData userData);

    Optional<UserData> findUser(String email, String password);
}
