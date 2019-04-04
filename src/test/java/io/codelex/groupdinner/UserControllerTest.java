package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import io.codelex.groupdinner.repository.model.User;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class UserControllerTest {

    private AttendeeService attendeeService = Mockito.mock(AttendeeService.class);
    private DinnerService dinnerService = Mockito.mock(DinnerService.class);
    private UserModule userModule = new UserModule(attendeeService, dinnerService);
    private User user = createUser();
    private String location = createLocation();
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
    
    private CreateDinnerRequest createDinnerRequest (User user, String location, LocalDateTime localDateTime) {
        return new CreateDinnerRequest(
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