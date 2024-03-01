package dhbw.mosbach.hotel.responseclasses;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseHotel {
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
