package dhbw.mosbach.trip.clients;

import dhbw.mosbach.trip.dtos.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("HOTELS/hotels/api/v1")
public interface HotelClient {
    @GetMapping("/hotel/{id}")
    Hotel getHotelById(@PathVariable("id") Long id);
}
