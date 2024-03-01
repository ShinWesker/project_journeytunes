package dhbw.mosbach.trip.clients;

import dhbw.mosbach.trip.dtos.RequestToken;
import dhbw.mosbach.trip.dtos.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("USERS")
public interface UserClient {
    @PostMapping("/get")
    public ResponseEntity<User> getUser(@RequestBody RequestToken reqToken);
}
