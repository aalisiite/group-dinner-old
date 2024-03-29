package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.JoinDinnerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserModule userModule;

    public UserController(UserModule userModule) {
        this.userModule = userModule;
    }

    @PostMapping("/dinners")
    public ResponseEntity<Dinner> createDinner(@Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userModule.createDinner(request), HttpStatus.CREATED);
    }

    @PostMapping("/dinners/{id}")
    //if we pass {id} here, does JoinDinnerRequest need to include dinner info too or just user
    public ResponseEntity<Attendee> joinDinner(
            @PathVariable Long id,
            @Valid @RequestBody JoinDinnerRequest request) {
        return new ResponseEntity<>(userModule.joinDinner(request), HttpStatus.OK);
    }
}
