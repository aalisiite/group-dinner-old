package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Feedback;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.CreateDinnerRequest;
import io.codelex.groupdinner.api.request.LeaveFeedbackRequest;
import io.codelex.groupdinner.repository.service.DinnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final DinnerService dinnerService;

    public UserController(DinnerService dinnerService) {
        this.dinnerService = dinnerService;
    }

    @PostMapping("/create-dinner")
    public ResponseEntity<Dinner> createDinner(
            Principal principal,
            @Valid @RequestBody CreateDinnerRequest request) {
        return new ResponseEntity<>(dinnerService.createDinner(principal.getName(), request), HttpStatus.CREATED);
    }

    @PostMapping("/dinners/{id}/join")
    public ResponseEntity<Attendee> joinDinner(
            Principal principal,
            @PathVariable Long id) {
        return new ResponseEntity<>(dinnerService.joinDinner(principal.getName(), id), HttpStatus.OK);
    }

    @PostMapping("/dinners/{id}/feedback")
    public ResponseEntity<Feedback> leaveFeedback(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody LeaveFeedbackRequest request) {
        return new ResponseEntity<>(dinnerService.leaveFeedback(principal.getName(), id, request), HttpStatus.CREATED);
    }

    @GetMapping("/dinners/{id}")
    public ResponseEntity<Dinner> getDinner(@PathVariable("id") Long id) {
        return new ResponseEntity<>(dinnerService.findDinner(id), HttpStatus.OK);
    }

    @GetMapping("/dinners/{id}/attendees")
    public ResponseEntity<List<User>> getDinnerAttendees(
            @PathVariable("id") Long id,
            @RequestParam("accepted") Boolean accepted) {
        return new ResponseEntity<>(dinnerService.findDinnerAttendees(id, accepted), HttpStatus.OK);
    }

    @GetMapping("/dinners")
    public ResponseEntity<List<Dinner>> getDinners(Principal principal) {
        return new ResponseEntity<>(dinnerService.getGoodMatchDinners(principal.getName()), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Exception caught: ", e);
    }

}
