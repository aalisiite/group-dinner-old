package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.model.LoginDetails;

public interface LoginService {
    void save(LoginDetails loginDetails);

    LoginDetails findByUsername(String username);
}
