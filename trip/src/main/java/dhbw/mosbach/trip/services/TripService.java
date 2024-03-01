package dhbw.mosbach.trip.services;

import dhbw.mosbach.trip.models.Trip;
import dhbw.mosbach.trip.repositories.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TripService {

    private TripRepository repository;

    public Trip saveTrip(Trip trip) {
        return repository.save(trip);
    }

    public List<Trip> saveTrips(List<Trip> trip) {
        return repository.saveAll(trip);
    }

    public List<Trip> getTrips() {
        return repository.findAll();
    }

    public Trip getTripById(long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Trip> findByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    public String deleteTrip(long id) {
        repository.deleteById(id);
        return "Trip with id " + id + " removed!";
    }


}