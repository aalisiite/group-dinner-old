package io.codelex.groupdinner.inMemory;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.CreateDinnerRequest;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.inMemory.service.AttendeeService;
import io.codelex.groupdinner.inMemory.service.DinnerService;
import io.codelex.groupdinner.inMemory.service.UsersService;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class InMemoryUserServiceTest {

    private AttendeeService attendeeService = Mockito.mock(AttendeeService.class);
    private DinnerService dinnerService = Mockito.mock(DinnerService.class);
    private UsersService usersService = Mockito.mock(UsersService.class);
    private InMemoryUserService userModule = new InMemoryUserService(attendeeService, dinnerService, usersService);
    private LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 1, 0, 0);
    private User user = createUser();
    private String location = createLocation();
    private Dinner dinner = createDinner();
    private CreateDinnerRequest dinnerRequest = createDinnerRequest();
    private Attendee attendee = createAcceptedAttendee();
    private Principal principal = () -> "1";

    @Test
    public void should_be_able_to_create_dinner() {
        //given

        //when
        Mockito.when(dinnerService.addDinner(any()))
                .thenReturn(dinner);
        Mockito.when(attendeeService.addAttendee(any()))
                .thenReturn(attendee);

        Dinner result = userModule.createDinner(dinnerRequest);

        //then
        assertEquals(dinner, result);
    }


    @Test
    public void should_be_able_to_join_event_with_accepted_status() {
        //given
        int initialGuestCount = dinner.getCurrentGuests();

        //when
        Mockito.when(dinnerService.getDinner(any()))
                .thenReturn(Optional.of(dinner));
        Mockito.when(usersService.getUser(any()))
                .thenReturn(Optional.of(user));
        Mockito.when(attendeeService.addAttendee(any()))
                .thenReturn(attendee);
        Attendee result = userModule.joinDinner(principal.getName(), dinner.getId());

        //then
        assertEquals(initialGuestCount + 1, dinner.getCurrentGuests());
        assertTrue(result.getIsAccepted());
    }


    @Test
    public void should_be_able_to_join_event_with_pending_status() {
        //given
        dinner.setCurrentGuests(dinner.getMaxGuests() + 1);
        int initialGuestCount = dinner.getCurrentGuests();

        //when
        Mockito.when(dinnerService.getDinner(any()))
                .thenReturn(Optional.of(dinner));
        Mockito.when(usersService.getUser(any()))
                .thenReturn(Optional.of(user));
        Mockito.when(attendeeService.addAttendee(any()))
                .thenReturn(attendee);
        Attendee result = userModule.joinDinner(principal.getName(), dinner.getId());

        //then
        assertEquals(initialGuestCount + 1, dinner.getCurrentGuests());
        assertFalse(result.getIsAccepted());
    }


    private Attendee createAcceptedAttendee() {
        return new Attendee(
                1L,
                dinner,
                user,
                true
        );
    }

    private User createUser() {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
        );
    }

    private CreateDinnerRequest createDinnerRequest() {
        return new CreateDinnerRequest(
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private Dinner createDinner() {
        return new Dinner(
                1L,
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private String createLocation() {
        return "Jurmalas Gatve 76";
    }

}