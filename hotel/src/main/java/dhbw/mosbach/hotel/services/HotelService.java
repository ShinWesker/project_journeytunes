package dhbw.mosbach.hotel.services;

import dhbw.mosbach.hotel.dtos.ResponseHotel;
import dhbw.mosbach.hotel.models.ContactData;
import dhbw.mosbach.hotel.models.Hotel;
import dhbw.mosbach.hotel.models.Location;
import dhbw.mosbach.hotel.repositories.ContactDataRepository;
import dhbw.mosbach.hotel.repositories.HotelRepository;
import dhbw.mosbach.hotel.repositories.HotelSpecifications;
import dhbw.mosbach.hotel.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {

    private HotelRepository hotelRepository;
    private LocationRepository locationRepository;
    private ContactDataRepository contactDataRepository;

    public ResponseHotel saveHotel(ResponseHotel responseHotel) {
        if (!checkIfAlreadyExists(responseHotel)) {
            Hotel hotel = hotelFromResponseHotel(responseHotel);
            Location location = locationFromResponseHotel(responseHotel);
            ContactData contactData = contactDataFromResponseHotel(responseHotel);
            hotel.setLocationId(location);
            hotel.setContactDataId(contactData);
            locationRepository.save(location);
            contactDataRepository.save(contactData);
            hotelRepository.save(hotel);
            return responseHotel;
        }
        return null;
    }

    public List<ResponseHotel> getHotels() {
        return mapHotelsToResponseHotels(hotelRepository.findAll());
    }

    public ResponseHotel getHotelById(long id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel == null) {
            return null;
        } else {
            return new ResponseHotel(hotel.getId(), hotel.getName(), hotel.getPricePerNight(), hotel.getDescription(), hotel.getImageData(), hotel.getStars(), hotel.getLocationId().getLongitude(), hotel.getLocationId().getLatitude(), hotel.getLocationId().getAddress(), hotel.getContactDataId().getPhoneNumber(), hotel.getContactDataId().getEmail(), hotel.getLocationId().getRegion(), hotel.getImageLink(), hotel.getCreator());
        }
    }

    public ResponseHotel updateHotel(ResponseHotel hotel) {
        Hotel existingHotel = hotelRepository.findById(hotel.getId()).orElse(null);

        if (existingHotel != null) {
            Location existingLocation = locationRepository.findById(existingHotel.getLocationId().getId()).orElse(null);
            ContactData existingContactData = contactDataRepository.findById(existingHotel.getContactDataId().getId()).orElse(null);
            if (existingLocation != null && existingContactData != null) {
                existingHotel.setName(hotel.getName());
                existingHotel.setDescription(hotel.getDescription());
                existingHotel.setStars(hotel.getStars());
                existingHotel.setPricePerNight(hotel.getPricePerNight());
                existingLocation.setAddress(hotel.getAddress());
                existingLocation.setRegion(hotel.getRegion());
                existingLocation.setLongitude(hotel.getLongitude());
                existingLocation.setLatitude(hotel.getLatitude());
                existingContactData.setEmail(hotel.getEmail());
                existingContactData.setPhoneNumber(hotel.getPhoneNumber());
                hotelRepository.save(existingHotel);
                locationRepository.save(existingLocation);
                contactDataRepository.save(existingContactData);
                return new ResponseHotel(existingHotel.getId(), hotel.getName(), hotel.getPricePerNight(), hotel.getDescription(), hotel.getImageData(), hotel.getStars(), hotel.getLongitude(), hotel.getLatitude(), hotel.getAddress(), hotel.getPhoneNumber(), hotel.getEmail(), hotel.getRegion(), hotel.getImageLink(), hotel.getCreator());

            }
        }
        return null;
    }

    public String deleteHotel(long id) {
        hotelRepository.deleteById(id);
        locationRepository.deleteById(id);
        contactDataRepository.deleteById(id);
        return "deleted hotel with id " + id;
    }


    public List<ResponseHotel> search(String region, Double pricePerNight, Integer stars, String name) {
        Specification<Hotel> spec = Specification.where(null);
        if (region != null && !region.isEmpty()) spec = spec.and(HotelSpecifications.hasRegion(region));
        if (pricePerNight != null) spec = spec.and(HotelSpecifications.pricePerNightLessThanEqual(pricePerNight));
        if (stars != null) spec = spec.and(HotelSpecifications.hasStars(stars));
        if (name != null && !name.isEmpty()) spec = spec.and(HotelSpecifications.nameContains(name));
        return mapHotelsToResponseHotels(hotelRepository.findAll(spec));
    }

    public List<ResponseHotel> getHotelsByOwner(long id) {
        return mapHotelsToResponseHotels(hotelRepository.findAllByCreator(id));
    }

    private List<ResponseHotel> mapHotelsToResponseHotels(List<Hotel> hotels) {
        List<ResponseHotel> responseHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            if (hotel.getLocationId() != null && hotel.getContactDataId() != null) {
                responseHotels.add(new ResponseHotel(hotel.getId(), hotel.getName(), hotel.getPricePerNight(), hotel.getDescription(), hotel.getImageData(), hotel.getStars(), hotel.getLocationId().getLongitude(), hotel.getLocationId().getLatitude(), hotel.getLocationId().getAddress(), hotel.getContactDataId().getPhoneNumber(), hotel.getContactDataId().getEmail(), hotel.getLocationId().getRegion(), hotel.getImageLink(), hotel.getCreator()));
            }
        }
        return responseHotels;
    }

    private Location locationFromResponseHotel(ResponseHotel responseHotel) {
        Location location = new Location();
        location.setLongitude(responseHotel.getLongitude());
        location.setLatitude(responseHotel.getLatitude());
        location.setAddress(responseHotel.getAddress());
        location.setRegion(responseHotel.getRegion());
        return location;
    }

    private ContactData contactDataFromResponseHotel(ResponseHotel responseHotel) {
        ContactData contactData = new ContactData();
        contactData.setEmail(responseHotel.getEmail());
        contactData.setPhoneNumber(responseHotel.getPhoneNumber());
        return contactData;
    }

    private Hotel hotelFromResponseHotel(ResponseHotel responseHotel) {
        Hotel hotel = new Hotel();
        hotel.setCreator(responseHotel.getCreator());
        hotel.setStars(responseHotel.getStars());
        hotel.setImageData(responseHotel.getImageData());
        hotel.setName(responseHotel.getName());
        hotel.setDescription(responseHotel.getDescription());
        hotel.setPricePerNight(responseHotel.getPricePerNight());
        return hotel;
    }

    private boolean checkIfAlreadyExists(ResponseHotel responseHotel) {
        boolean hotel
                = hotelRepository.existsByNameAndPricePerNightAndDescriptionAndImageDataAndStarsAndCreator(responseHotel.getName(), responseHotel.getPricePerNight(), responseHotel.getDescription(), responseHotel.getImageData(), responseHotel.getStars(), responseHotel.getCreator());
        boolean location = locationRepository.existsByLongitudeAndLatitudeAndAddressAndRegion(responseHotel.getLongitude(), responseHotel.getLatitude(), responseHotel.getAddress(), responseHotel.getRegion());
        boolean contactData = contactDataRepository.existsByEmailAndPhoneNumber(responseHotel.getEmail(), responseHotel.getPhoneNumber());
        return hotel && location && contactData;
    }

}
