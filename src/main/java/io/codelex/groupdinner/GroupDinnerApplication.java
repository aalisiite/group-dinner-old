package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Attendee;
import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.Location;
import io.codelex.groupdinner.api.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GroupDinnerApplication {

    public static void main(String[] args) {

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
                user1,
                1L,
                "Test Dinner",
                2,
                "description",
                location,
                LocalDateTime.now().plusDays(2)
        );

        userModule.joinDinner(user2, dinner);
        userModule.joinDinner(user3, dinner);


        for (Attendee attendee : attendeeService.attendees) {
            System.out.println(attendee.getDinner().getTitle());
            System.out.println(attendee.getStatus());

        }


        //SpringApplication.run(GroupDinnerApplication.class, args);
    }

}
