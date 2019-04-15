package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/dinners/{id}/feedback")
    public ResponseEntity<Feedback> leaveFeedback(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody LeaveFeedbackRequest request) {
        return new ResponseEntity<>(userService.leaveFeedback(principal.getName(), id, request), HttpStatus.OK);
    }

    @GetMapping("/dinners/{id}")
    public ResponseEntity<Dinner> getDinner(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinner(id), HttpStatus.OK);
    }
    
    @GetMapping("/dinners/{id}/accepted")
    public ResponseEntity<List<User>> getAcceptedDinnerAttendees (@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinnerAttendees(id, true), HttpStatus.OK);
    }
    
    @GetMapping("/dinners/{id}/pending")
    public ResponseEntity<List<User>> getPendingDinnerAttendees (@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinnerAttendees(id, false), HttpStatus.OK);
    }


}
