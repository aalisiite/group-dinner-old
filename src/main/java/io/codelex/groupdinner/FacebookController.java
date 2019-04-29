package io.codelex.groupdinner;

import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import io.codelex.groupdinner.repository.service.AuthContextService;
import io.codelex.groupdinner.repository.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.codelex.groupdinner.repository.service.Role.REGISTERED_CLIENT;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FacebookController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final AuthContextService authContext;

    public FacebookController(AuthService authService, AuthContextService authContext) {
        this.authService = authService;
        this.authContext = authContext;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<User> signIn(@Valid @RequestBody SignInRequest request) {
        User user = authService.authenticateUser(request);
        authContext.authorise(request.getEmail().trim().toLowerCase(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistrationRequest request) {
        User user = authService.registerUser(request);
        authContext.authorise(request.getEmail().trim().toLowerCase(), request.getPassword(), REGISTERED_CLIENT);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}