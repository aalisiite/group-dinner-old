package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SigninRequest;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.service.AuthService;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.security.Principal;

import static io.codelex.groupdinner.repository.service.Role.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    private final AuthService authService;
    private RepositoryUserService service;

    AuthController(RepositoryUserService repositoryUserService, AuthService authService) {
        this.service = repositoryUserService;
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<User> signIn(@Valid @RequestBody SigninRequest request) {
        authService.authorise(request.getEmail(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(service.authenticateUser(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegistrationRequest request) {
        authService.authorise(request.getEmail(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(service.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/sign-out")
    public void signOut() {
        authService.clearAuthentication();
    }

    @GetMapping("/account")
    public String account(Principal principal) {
        return principal.getName();
    }
}
