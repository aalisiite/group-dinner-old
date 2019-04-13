package io.codelex.groupdinner.authorization;

import io.codelex.groupdinner.authorization.api.UserData;
import io.codelex.groupdinner.authorization.service.RegistrationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static io.codelex.groupdinner.authorization.Role.*;

@RestController
@RequestMapping("/api")
class CustomerController {
    private final AuthService authService;
    @Autowired
    RegistrationDataService service;

    CustomerController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserData> signIn(@RequestParam("email") String email,
                                           @RequestParam("password") String password) {
        authService.authorise(email, password, REGISTERED_CLIENT);
        return new ResponseEntity<>(service.findUser(email, password), HttpStatus.OK);
    }

    @PostMapping("/register")
    public void register(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        authService.authorise(email, password, REGISTERED_CLIENT);
        service.addUser(new UserData(email, password));
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
