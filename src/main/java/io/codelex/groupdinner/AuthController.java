package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SignInRequest;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.service.RepositoryAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static io.codelex.groupdinner.repository.service.Role.REGISTERED_CLIENT;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final RepositoryAuthService authService;

    public AuthController(RepositoryAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<User> signIn(@Valid @RequestBody SignInRequest request) {
        User user = authService.authenticateUser(request);
        authService.authorise(request.getEmail().trim().toLowerCase(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegistrationRequest request) {
        User user = authService.registerUser(request);
        authService.authorise(request.getEmail().trim().toLowerCase(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
