package dhbw.mosbach.trip.requestclasses;

import dhbw.mosbach.trip.models.Trip;
import lombok.AllArgsConstructor;
@AllArgsConstructor
public class CreateTrip {
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private long hotelId;
    private String playlistLink;

    public Trip createTrip(long userId){
        return  new Trip(this.startLat, this.startLng, this.endLat, this.endLng, this.hotelId, userId, this.playlistLink);
    }

}
