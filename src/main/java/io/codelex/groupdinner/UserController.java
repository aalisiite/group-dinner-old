package io.codelex.groupdinner;

import io.codelex.groupdinner.InMemory.InMemoryUserModule;
import io.codelex.groupdinner.repository.AttendeeRecordRepository;
import io.codelex.groupdinner.repository.model.AttendeeRecord;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.api.JoinDinnerRequest;
import io.codelex.groupdinner.InMemory.service.AttendeeService;
import io.codelex.groupdinner.InMemory.service.DinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserModule userModule;

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
