package dhbw.mosbach.trip.repositories;

import dhbw.mosbach.trip.models.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(long userId);

    void deleteAllByHotelId(long id);
}