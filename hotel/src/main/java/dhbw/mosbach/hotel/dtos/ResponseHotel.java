package dhbw.mosbach.hotel.dtos;


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
    private double longitude;
    private double latitude;
    private String address;
    private long phoneNumber;
    private String email;
    private String region;
    private String imageLink;
    private int creator;
}
