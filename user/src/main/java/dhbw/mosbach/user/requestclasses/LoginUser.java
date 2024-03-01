package dhbw.mosbach.user.requestclasses;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginUser {
    private String name;
    private String password;
}