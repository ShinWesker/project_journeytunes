package dhbw.mosbach.trip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
}
