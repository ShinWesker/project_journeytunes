package dhbw.mosbach.user.requestclasses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Email {
    private String to;
    private String subject;
    private String body;
}
