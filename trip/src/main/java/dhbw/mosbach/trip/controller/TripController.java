package dhbw.mosbach.trip.controller;

import dhbw.mosbach.trip.clients.HotelClient;
import dhbw.mosbach.trip.clients.UserClient;
import dhbw.mosbach.trip.dtos.*;
import dhbw.mosbach.trip.models.Trip;
import dhbw.mosbach.trip.services.TripService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/trips/api/v1")
public class TripController {
    private TripService tripService;
    private UserClient userClient;
    private HotelClient hotelClient;

    @PostMapping("/create")
    public ResponseEntity<Object> createTrip(@RequestBody CreateTrip reqTrip) {

        User user = userClient.getUserById(reqTrip.getUserId()).getBody();
        if (user == null)
            return new ResponseEntity<>("User could not be found!", HttpStatus.NOT_FOUND);

        Hotel hotel = hotelClient.getHotelById(reqTrip.getHotelId());
        if (hotel == null)
            return new ResponseEntity<>("Hotel could not be found!", HttpStatus.NOT_FOUND);
        Trip trip = reqTrip.createTrip(hotel.getLatitude(), hotel.getLongitude());
        tripService.saveTrip(trip);
        return new ResponseEntity<>(trip, HttpStatus.CREATED);
    }

    @GetMapping("/journeys")
    public ResponseEntity<Object> getTrip(@RequestHeader("Authorization") String authorization) {
        String tokenValue = extractTokenValue(authorization);
        User user = userClient.getUser(new RequestToken(tokenValue)).getBody();
        if (user == null)
            return new ResponseEntity<>("User could not be found!", HttpStatus.NOT_FOUND);

        List<Trip> trips = tripService.findByUserId(user.getId());
        if (trips == null)
            return new ResponseEntity<>("No Trips for this user registered!", HttpStatus.NOT_FOUND);
        List<FullTrip> outTrips = new ArrayList<>();
        for (Trip trip : trips) {
            Hotel hotel = hotelClient.getHotelById(trip.getHotelId());
            outTrips.add(new FullTrip(hotel, trip.getPlaylistLink(), new Route(trip.getStartLat(), trip.getStartLng(), trip.getEndLat(), trip.getEndLng())));
        }
        return new ResponseEntity<>(outTrips, HttpStatus.OK);
    }

    private String extractTokenValue(String authorizationHeader) {
        return authorizationHeader.split(" ")[1];
    }

}
