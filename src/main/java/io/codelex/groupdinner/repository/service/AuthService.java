package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    private final UserRecordRepository userRecordRepository;
    private final MapDBRecordToApiCompatible toApiCompatible = new MapDBRecordToApiCompatible();
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRecordRepository userRecordRepository,
            PasswordEncoder passwordEncoder) {
        this.userRecordRepository = userRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegistrationRequest request) {
        if (!userRecordRepository.isUserPresent(request.getEmail())) {
            UserRecord user = new UserRecord(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail().toLowerCase().trim(),
                    passwordEncoder.encode(request.getPassword())
            );
            user = userRecordRepository.save(user);
            return toApiCompatible.apply(user);
        } else {
            throw new IllegalStateException("Email already exists");
        }
    }

    public User authenticateUser(SignInRequest request) {
        UserRecord user = userRecordRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return toApiCompatible.apply(user);
            } else {
                throw new IllegalStateException("Password incorrect");
            }
        } else {
            throw new IllegalStateException("Email incorrect");
        }
    }

}
