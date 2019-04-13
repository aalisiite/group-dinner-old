package io.codelex.groupdinner.authorization;

import io.codelex.groupdinner.authorization.api.UserData;
import io.codelex.groupdinner.authorization.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static io.codelex.groupdinner.authorization.Role.*;

@RestController
@RequestMapping("/api")
class CustomerController {
    private final AuthService authService;
    @Autowired
    UserDataService service;

    CustomerController(AuthService authService) {
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
