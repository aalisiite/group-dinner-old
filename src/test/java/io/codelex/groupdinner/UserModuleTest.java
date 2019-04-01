package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Location;
import io.codelex.groupdinner.api.User;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UserModuleTest {

    private AttendeeService attendeeService = Mockito.mock(AttendeeService.class);
    private DinnerService dinnerService = Mockito.mock(DinnerService.class);
    private UserModule userModule = new UserModule(attendeeService, dinnerService);
    private LocalDateTime localDateTime = LocalDateTime.of(2019,1,1,0,0);
    private User user = createUser();
    private Location location = createLocation();
    private Dinner dinner = createDinner(user, location);
    private Attendee attendee = createAttendee(user, dinner);
    
    @Test
    public void should_be_able_to_create_dinner() {
        //given

        //when
        Mockito.when(dinnerService.addDinner(any()))
                .thenReturn(dinner);
        Mockito.when(attendeeService.addAttendee(any()))
                .thenReturn(attendee);
        
        Dinner result = userModule.createDinner(
                user, 
                dinner.getId(), 
                dinner.getTitle(),
                dinner.getMaxGuests(),
                dinner.getDescription(),
                dinner.getLocation(),
                dinner.getDateTime()
        );
        
        //then
        assertEquals(dinner, result);
        
    }
    
    @Test
    public void should_be_able_to_join_event_with_accepted_status () {
        //given
        int initialGuestCount = dinner.getCurrentGuests();
        
        //when
        userModule.joinDinner(user, dinner);
        
        //then
        assertEquals(initialGuestCount + 1, dinner.getCurrentGuests());
    }
    
    
    @Test
    public void should_be_able_to_join_event_with_pending_status () {
        //given
        dinner.setCurrentGuests(dinner.getMaxGuests()+1);
        int initialGuestCount = dinner.getCurrentGuests();
        
        //when
        userModule.joinDinner(user, dinner);
        
        //then
        assertEquals(initialGuestCount + 1, dinner.getCurrentGuests());
    }
    
    
    
    
    
    
    private Attendee createAttendee(User user, Dinner dinner) {
        return new Attendee(
                    dinner,
                    user,
                    true
            );
    }
    
    private Dinner createDinner(User user, Location location) {
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
    
    private Location createLocation() {
        return new Location(
                    "Jurmalas Gatve",
                    76
            );
    }
    
    private User createUser () {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
        );
    }

}