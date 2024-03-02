package dhbw.mosbach.hotel.clients;

import dhbw.mosbach.hotel.dtos.Location;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("ROUTES/routes/api/v1/")
public interface RouteClient {

    @GetMapping("/coordinates")
    ResponseEntity<Location> getCoordinates(@RequestParam("text") String text);
}
