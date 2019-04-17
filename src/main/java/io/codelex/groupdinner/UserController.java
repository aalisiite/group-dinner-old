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
@RequestMapping("/api/dinners")
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping//todo remove dinners
    public ResponseEntity<Dinner> createDinner(
            Principal principal,
            @Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userService.createDinner(principal.getName(), request), HttpStatus.CREATED);
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
    public ResponseEntity<List<User>> getAcceptedDinnerAttendees(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinnerAttendees(id, true), HttpStatus.OK);
    }

    @GetMapping("/dinners/{id}/pending")
    public ResponseEntity<List<User>> getPendingDinnerAttendees(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinnerAttendees(id, false), HttpStatus.OK);
    }

    @GetMapping("/dinners")
    public ResponseEntity<List<Dinner>> getDinners(Principal principal) {
        return new ResponseEntity<>(userService.getGoodMatchDinners(principal.getName()), HttpStatus.OK);
    }


}
