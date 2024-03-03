package dhbw.mosbach.hotel.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("TRIPS/trips/api/v1")
public interface TripClient {

    @DeleteMapping("/delete/{hotel-id}")
    ResponseEntity<String> deleteTripForHotel(@PathVariable("hotel-id") long hotelId);
}
