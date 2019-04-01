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

    @Test
    public void should_return_dinner_and_status_created () {
        //given
        User user = createUser();
        
        CreateDinnerRequest request = createDinnerRequest()
        
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
    
    /*//given
        FindTripRequest request = createFindTripRequest();
        Trip trip = new Trip(
                1L,
                request.getFrom(),
                request.getTo(),
                "Ryanair",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
        String jsonRequest = MAPPER.writeValueAsString(request);

        TripWithForecast tripWithForecast = createTripWithForecast();


        //when
        Mockito.lenient()
                .when(service.findTrip(any()))
                .thenReturn(Collections.singletonList(trip)
                );

        Mockito.lenient()
                .when(mapToTripWithForecast.findTrips(request))
                .thenReturn(Collections.singletonList(tripWithForecast));

        //expect
        String jsonResponse = mockMvc.perform(
                post("/api/flights")
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<TripWithForecast> result = MAPPER.readValue(
                jsonResponse, new TypeReference<List<TripWithForecast>>() {
                }
        );

        //and
        assertEquals(trip.getFrom().getAirport(), result.get(0).getFrom().getAirport());
        assertEquals(trip.getTo().getAirport(), result.get(0).getTo().getAirport());*/
}