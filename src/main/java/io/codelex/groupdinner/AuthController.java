package io.codelex.groupdinner;

import io.codelex.groupdinner.api.RegistrationRequest;
import io.codelex.groupdinner.api.SignInRequest;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.repository.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static io.codelex.groupdinner.repository.service.Role.REGISTERED_CLIENT;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalState(IllegalStateException e) {
        log.warn("Exception caught: ", e);
    }
}
