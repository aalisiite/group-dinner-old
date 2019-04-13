package io.codelex.groupdinner;

import io.codelex.groupdinner.api.UserData;

import java.util.Optional;

public interface UserDataService {

    Optional<UserData> addUser(UserData userData);

    Optional<UserData> findUser(String email, String password);
}
