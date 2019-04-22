package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.AuthService;
import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SignInRequest;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.mapper.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.util.Collections.singleton;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class RepositoryAuthService implements AuthService {

    private final UserRecordRepository userRecordRepository;
    private final MapDBRecordToApiCompatible toApiCompatible = new MapDBRecordToApiCompatible();
    private final PasswordEncoder passwordEncoder;

    public RepositoryAuthService(
            UserRecordRepository userRecordRepository,
            PasswordEncoder passwordEncoder) {
        this.userRecordRepository = userRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void authorise(String email, String password, Role role) {
        var authorities = singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
        var token = new UsernamePasswordAuthenticationToken(email, password, authorities);
        getContext().setAuthentication(token);
    }

    public void clearAuthentication() {
        getContext().setAuthentication(null);
    }

    @Override
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

    @Override
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
