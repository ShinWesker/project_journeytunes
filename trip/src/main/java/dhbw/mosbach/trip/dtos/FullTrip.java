package dhbw.mosbach.trip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FullTrip {
    private Hotel hotel;
    private String playlistLink;
    private Route route;
}
