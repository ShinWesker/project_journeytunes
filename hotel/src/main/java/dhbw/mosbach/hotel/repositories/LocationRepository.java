package dhbw.mosbach.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dhbw.mosbach.hotel.models.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLongitudeAndLatitudeAndAddressAndRegion(
            double longitude, double latitude, String address, String region);
}
