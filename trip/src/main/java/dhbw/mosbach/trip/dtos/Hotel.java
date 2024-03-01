package dhbw.mosbach.trip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hotel {
    private long id;
    private String name;
    private double pricePerNight;
    private String description;
    private byte[] imageData;
    private int stars;
    private long longitude;
    private long latitude;
    private String address;
    private long phoneNumber;
    private String email;
    private String region;
    private String imageLink;
    private int creator;
}
