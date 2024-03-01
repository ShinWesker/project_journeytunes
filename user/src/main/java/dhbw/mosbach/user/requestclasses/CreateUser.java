package dhbw.mosbach.user.requestclasses;

import dhbw.mosbach.user.models.User;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class CreateUser {
    private String name;
    private String password;
    private String email;

    public User createUser(){
        return  new User(name, password, email, false);
    }
}
