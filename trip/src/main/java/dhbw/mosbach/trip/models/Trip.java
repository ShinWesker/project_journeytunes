package dhbw.mosbach.trip.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Trip {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private long hotelId;
    private long userId;
    private String playlistLink;

    public Trip(String startLat, String startLng, String endLat, String endLng, long hotelId, long userId, String playlistLink) {
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.hotelId = hotelId;
        this.userId = userId;
        this.playlistLink = playlistLink;
    }

    public Trip() {

    }
}
