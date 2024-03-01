package dhbw.mosbach.trip.dtos;

import dhbw.mosbach.trip.models.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateTrip {
    private long startLat;
    private long startLng;
    private long hotelId;
    private long userId;
    private String playlistLink;

    public Trip createTrip(long endLat, long endLng) {
        return new Trip(this.startLat, this.startLng, endLat, endLng, this.hotelId, this.userId, this.playlistLink);
    }

}
