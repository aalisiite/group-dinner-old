package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/dinners")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //todo catch exceptions + response codes

    @PostMapping
    public ResponseEntity<Dinner> createDinner(
            Principal principal,
            @Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(userService.createDinner(principal.getName(), request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Attendee> joinDinner(
            Principal principal,
            @PathVariable Long id) {
        return new ResponseEntity<>(userService.joinDinner(principal.getName(), id), HttpStatus.OK);
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<Feedback> leaveFeedback(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody LeaveFeedbackRequest request) {
        return new ResponseEntity<>(userService.leaveFeedback(principal.getName(), id, request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dinner> getDinner(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDinner(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/attendees")
    public ResponseEntity<List<User>> getAcceptedDinnerAttendees(
            @PathVariable("id") Long id,
            @RequestParam("accepted") Boolean accepted) {
        return new ResponseEntity<>(userService.findDinnerAttendees(id, accepted), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Dinner>> getDinners(Principal principal) {
        return new ResponseEntity<>(userService.getGoodMatchDinners(principal.getName()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalState(IllegalStateException e){
        log.warn("Exception caught: ", e);
    }
}
