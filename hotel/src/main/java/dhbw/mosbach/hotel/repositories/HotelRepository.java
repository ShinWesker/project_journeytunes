package dhbw.mosbach.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import dhbw.mosbach.hotel.models.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    List<Hotel> findAllByCreator(long id);

    boolean existsByNameAndPricePerNightAndDescriptionAndImageDataAndStarsAndCreator(
            String name, double pricePerNight, String description, byte[] imageData, int stars,  int creator);
}
