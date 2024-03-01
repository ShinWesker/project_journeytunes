package dhbw.mosbach.trip.repositories;

import dhbw.mosbach.trip.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(long userId);
}