package io.codelex.groupdinner.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserAuthorizationService  {

    private List<AuthRequest> registrations = new ArrayList<>();

    public InMemoryUserAuthorizationService() {
    }

    public Optional<AuthRequest> addUser(AuthRequest authRequest) {
        if (!isUserDataMatching(authRequest)) {
            AuthRequest lowerCaseData = new AuthRequest(
                    authRequest.getEmail().toLowerCase(),
                    authRequest.getPassword()
            );
            registrations.add(lowerCaseData);
            return Optional.of(lowerCaseData);
        }
        return Optional.empty();
    }

    private boolean isUserDataMatching(AuthRequest data) {
        AuthRequest lowerCaseData = new AuthRequest(
                data.getEmail().toLowerCase(),
                data.getPassword()
        );
        return registrations.contains(lowerCaseData);
    }

    public Optional<AuthRequest> findUser(String email, String password) {
        AuthRequest authRequest = new AuthRequest(
                email,
                password
        );
        for (AuthRequest user : registrations) {
            if (isUserDataMatching(authRequest)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
