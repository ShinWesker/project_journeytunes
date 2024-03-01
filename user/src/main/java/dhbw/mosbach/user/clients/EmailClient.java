package dhbw.mosbach.user.clients;

import dhbw.mosbach.user.requestclasses.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("EMAIL")
public interface EmailClient {
    @PostMapping("/send")
    void sendEmail(@RequestBody Email email);
}
