package dhbw.mosbach.trip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Route {
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
}
