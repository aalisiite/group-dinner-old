package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.JoinDinnerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private List<Attendee> attendees = new ArrayList<>();
    private AttendeeService attendeeService = new AttendeeService(attendees);
    private List<Dinner> dinners = new ArrayList<>();
    private DinnerService dinnerService = new DinnerService(dinners);
    private UserModule userModule = new UserModule(attendeeService, dinnerService);

    @PostMapping("/events")
    public ResponseEntity<Dinner> createDinner(@Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userModule.createDinner(request), HttpStatus.CREATED);
    }

    @PostMapping("/events/{id}")  //if we pass {id} here, does JoinDinnerRequest need to include dinner info too or just user
    public ResponseEntity<Boolean> joinDinner(
            @PathVariable Long id,
            @Valid @RequestBody JoinDinnerRequest request) {
        return new ResponseEntity<>(userModule.joinDinner(request), HttpStatus.OK);
    }


}
