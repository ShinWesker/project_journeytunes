package dhbw.mosbach.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double pricePerNight;
    private String description;
    @Lob
    @Column(name = "imageData", columnDefinition = "LONGBLOB")
    private byte[] imageData;
    private String imageLink;
    private int stars;
    private int creator;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location locationId;
    @OneToOne
    @JoinColumn(name = "contactdata_id")
    private ContactData contactDataId;
}
