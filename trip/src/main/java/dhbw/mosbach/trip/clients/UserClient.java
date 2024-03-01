package dhbw.mosbach.trip.clients;

import dhbw.mosbach.trip.dtos.RequestToken;
import dhbw.mosbach.trip.dtos.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("USERS/users/api/v1")
public interface UserClient {
    @PostMapping("/user/{id}")
    ResponseEntity<User> getUserById(@PathVariable long id);
    @PostMapping("/get")
    ResponseEntity<User> getUser(@RequestBody RequestToken reqToken);
}
