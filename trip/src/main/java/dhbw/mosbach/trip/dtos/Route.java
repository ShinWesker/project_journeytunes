package dhbw.mosbach.trip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Route {
    private long startLat;
    private long startLng;
    private long endLat;
    private long endLng;
}
