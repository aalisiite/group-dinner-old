package io.codelex.groupdinner;

import io.codelex.groupdinner.api.UserData;
import io.codelex.groupdinner.repository.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static io.codelex.groupdinner.repository.service.Role.*;

@RestController
@RequestMapping("/api")
public class CustomerController {
    
    private final AuthService authService;
    private UserDataService service;

    CustomerController(UserDataService userDataService, AuthService authService) {
        this.service = userDataService;
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Optional<UserData>> signIn(@RequestParam("email") String email,
                                                     @RequestParam("password") String password) {
        authService.authorise(email, password, REGISTERED_CLIENT);
        return new ResponseEntity<>(service.findUser(email, password), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<UserData>> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        authService.authorise(email, password, REGISTERED_CLIENT);
        return new ResponseEntity<>(service.addUser(new UserData(email, password)), HttpStatus.CREATED);
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
