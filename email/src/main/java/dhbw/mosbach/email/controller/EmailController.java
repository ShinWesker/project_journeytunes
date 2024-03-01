package dhbw.mosbach.email.controller;

import dhbw.mosbach.email.modules.Email;
import dhbw.mosbach.email.subservices.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails/api/v1")
public class EmailController {

    private EmailSenderService emailSenderService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody Email email) {
        System.out.println(email.getBody());
        emailSenderService.sendEmail(email);
    }

}
