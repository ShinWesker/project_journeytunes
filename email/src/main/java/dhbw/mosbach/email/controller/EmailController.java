package dhbw.mosbach.email.controller;

import dhbw.mosbach.email.modules.Email;
import dhbw.mosbach.email.subservices.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails/api/v1")
@AllArgsConstructor
public class EmailController {

    private EmailSenderService emailSenderService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody Email email) {
        emailSenderService.sendEmail(email);
    }

}
