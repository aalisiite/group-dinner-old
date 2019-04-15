package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SignInRequest;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.service.AuthService;
import io.codelex.groupdinner.repository.service.RepositoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static io.codelex.groupdinner.repository.service.Role.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final RepositoryUserService service;

    public AuthController(AuthService authService, RepositoryUserService service) {
        this.authService = authService;
        this.service = service;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<User> signIn(@Valid @RequestBody SignInRequest request) {
        User user = service.authenticateUser(request);
        authService.authorise(request.getEmail(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegistrationRequest request) {
        User user = service.registerUser(request);
        authService.authorise(request.getEmail(), request.getPassword(), REGISTERED_CLIENT);
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
