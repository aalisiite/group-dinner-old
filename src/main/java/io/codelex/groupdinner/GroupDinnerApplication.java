package io.codelex.groupdinner;

import io.codelex.groupdinner.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GroupDinnerApplication {

    public static void main(String[] args) {
/*
        User user1 = new User(
                1L,
                "Nikola",
                "Upite",
                "asdjad@gmai.com"
        );
        User user2 = new User(
                2L,
                "Alise",
                "Alka",
                "ajsdkhaskd@gmai.com"
        );
        User user3 = new User(
                3L,
                "Krists",
                "Plavnieks",
                "askjda@gmai.com"
        );
        Location location = new Location(
                "adress asdkas",
                2
        );

        List<Attendee> attendees = new ArrayList<>();
        List<Dinner> dinners = new ArrayList<>();
        AttendeeService attendeeService = new AttendeeService(attendees);
        DinnerService dinnerService = new DinnerService(dinners);

        UserModule userModule = new UserModule(attendeeService, dinnerService);

        Dinner dinner = userModule.createDinner(
                createDinnerRequest(createUser(), 
                        createLocation(), 
                        LocalDateTime.of(2019,1,1,0,0))
        );

        userModule.joinDinner(joinDinnerRequest(user2, dinner));
        userModule.joinDinner(joinDinnerRequest(user3, dinner));


        for (Attendee attendee : attendeeService.attendees) {
            System.out.println(attendee.getDinner().getTitle());
            System.out.println(attendee.getStatus());

        }
    }
    private static User createUser () {
        return new User(
                1L,
                "Janis",
                "Berzins",
                "berzins@gmai.com"
        );
    }

    private static JoinDinnerRequest joinDinnerRequest (User user, Dinner dinner) {
        return new JoinDinnerRequest(
                user,
                dinner
        );
    }
    private static CreateDinnerRequest createDinnerRequest (User user, Location location, LocalDateTime localDateTime) {
        return new CreateDinnerRequest(
                "This is a title",
                user,
                2,
                "This is a description",
                location,
                localDateTime
        );
    }

    private static Location createLocation() {
        return new Location(
                "Jurmalas Gatve",
                76
        );
    }
    
*/

        SpringApplication.run(GroupDinnerApplication.class, args);
}}


