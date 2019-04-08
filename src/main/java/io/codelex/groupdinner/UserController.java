package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/dinners")
    public ResponseEntity<Dinner> createDinner(@Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userService.createDinner(request), HttpStatus.CREATED);
    }

    @PostMapping("/dinners/{id}/join")
    public ResponseEntity<Attendee> joinDinner(
            Principal principal,
            @PathVariable Long id) {
        return new ResponseEntity<>(userService.joinDinner(principal.getName(), id), HttpStatus.OK);
    }

    @GetMapping("/dinners/{id}")
    public ResponseEntity<Attendee> findDinner() {
        return null;
    }


}
