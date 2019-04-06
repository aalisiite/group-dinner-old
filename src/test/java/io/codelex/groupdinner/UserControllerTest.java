package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserControllerTest {

    private AttendeeService attendeeService = Mockito.mock(AttendeeService.class);
    private DinnerService dinnerService = Mockito.mock(DinnerService.class);
    private UserModule userModule = new UserModule(attendeeService, dinnerService);
    private User user = createUser();
    private Location location = createLocation();
    private LocalDateTime localDateTime = LocalDateTime.of(2019,1,1,0,0);
    CreateDinnerRequest request = createDinnerRequest(user, location, localDateTime);
    
    @Test
    public void should_return_dinner_and_status_created () {
        //given
        
        //when
        
        //then
        
    }

    private User createUser () {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
        );
    }
    
    private CreateDinnerRequest createDinnerRequest (User user, Location location, LocalDateTime localDateTime) {
        return new CreateDinnerRequest(
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
    
}