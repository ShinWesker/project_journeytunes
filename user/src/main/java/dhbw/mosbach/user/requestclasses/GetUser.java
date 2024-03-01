package dhbw.mosbach.user.requestclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
@AllArgsConstructor
public class GetUser {
    private long id;
    private String name;
    private String email;
}
