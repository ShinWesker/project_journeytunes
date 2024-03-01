package dhbw.mosbach.trip.controller;

import dhbw.mosbach.trip.clients.UserClient;
import dhbw.mosbach.trip.dtos.RequestToken;
import dhbw.mosbach.trip.dtos.User;
import dhbw.mosbach.trip.models.Trip;
import dhbw.mosbach.trip.requestclasses.CreateTrip;
import dhbw.mosbach.trip.services.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/api/v1")
public class TripController {
    private TripService tripService;
    private UserClient userClient;

    @PostMapping("/create")
    public ResponseEntity<Object> createTrip(@RequestBody CreateTrip reqTrip,
            @RequestHeader("Authorization") String bearerToken) {

        User user = userClient.getUser(new RequestToken(bearerToken)).getBody();
        assert user != null;
        if (user.getId() == -1)
            return new ResponseEntity<>("User could not be found!", HttpStatus.NOT_FOUND);
        Trip trip = reqTrip.createTrip(user.getId());
        tripService.saveTrip(trip);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/journeys")
    public ResponseEntity<Object> getTrip(@RequestHeader("Authorization") String bearerToken) {

        User user = userClient.getUser(new RequestToken(bearerToken)).getBody();

        assert user != null;
        List<Trip> trips = tripService.findByUserId(user.getId());

        if (trips == null)
            return new ResponseEntity<>("No Trips for this user registered!", HttpStatus.NOT_FOUND);
        for (Trip trip : trips) {
            System.out.println(trip);
        }

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

}
