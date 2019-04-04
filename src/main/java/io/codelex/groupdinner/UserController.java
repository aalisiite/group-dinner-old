package io.codelex.groupdinner;

import io.codelex.groupdinner.InMemory.InMemoryUserModule;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.repository.model.DinnerRecord;
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

    private List<AttendeeRecord> attendees = new ArrayList<>();
    private AttendeeService attendeeService = new AttendeeService(attendees);
    private List<DinnerRecord> dinners = new ArrayList<>();
    private DinnerService dinnerService = new DinnerService(dinners);
    private InMemoryUserModule userModule = new InMemoryUserModule(attendeeService, dinnerService);

    @PostMapping("/dinners")
    public ResponseEntity<DinnerRecord> createDinner(@Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userModule.createDinner(request), HttpStatus.CREATED);
    }

    @PostMapping("/dinners/{id}")
    //if we pass {id} here, does JoinDinnerRequest need to include dinner info too or just user
    public ResponseEntity<Boolean> joinDinner(
            @PathVariable Long id,
            @Valid @RequestBody JoinDinnerRequest request) {
        return new ResponseEntity<>(userModule.joinDinner(request), HttpStatus.OK);
    }


}
