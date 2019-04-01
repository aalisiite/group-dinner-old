package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
   /* @PostMapping("/events/{id}")
    public ResponseEntity<Dinner> joinDinner(@Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userModule.joinDinner(request);, HttpStatus.OK);
    }*/
   

}
