package dhbw.mosbach.hotel.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dhbw.mosbach.hotel.responseclasses.ResponseHotel;
import dhbw.mosbach.hotel.services.HotelService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@AllArgsConstructor
@RequestMapping("/hotels/api/v1")
public class HotelController {

    private HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<ResponseHotel>> getHotels(@RequestParam(name = "limit", defaultValue = "0") int limit) {
        List<ResponseHotel> hotels = hotelService.getHotels();
        if (limit == 0) {
            for (ResponseHotel hotel : hotels) {
                hotel.setImageData(null);
                hotel.setImageLink("http://localhost:8222/hotels/api/v1/hotels/" + hotel.getId() + "/image");

            }
            return ResponseEntity.status(HttpStatus.OK).body(hotels);
        } else {
            List<ResponseHotel> outHotels = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                ResponseHotel hotel = hotels.get(ThreadLocalRandom.current().nextInt(1, hotels.size()));
                hotel.setImageData(null);
                hotel.setImageLink("http://localhost:8222/hotels/api/v1/hotels/" + hotel.getId() + "/image");
                if (!outHotels.contains(hotel)) {
                    outHotels.add(hotel);
                } else {
                    if (hotels.size() >= limit) {
                        i--;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(outHotels);
        }

    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<ResponseHotel> getHotelById(@PathVariable int id) {
        ResponseHotel hotel = hotelService.getHotelById(id);
        if (hotel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            hotel.setImageData(null);
            hotel.setImageLink("http://localhost:8222/hotels/api/v1/hotels/" + hotel.getId() + "/image");
            return ResponseEntity.status(HttpStatus.OK).body(hotel);

        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseHotel>> searchHotels(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Double pricePerNight,
            @RequestParam(required = false) Integer stars,
            @RequestParam(required = false) String name) {
        List<ResponseHotel> hotels = hotelService.search(region, pricePerNight, stars, name);
        if (hotels == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            for (ResponseHotel hotel : hotels) {
                hotel.setImageData(null);
                hotel.setImageLink("http://localhost:8222/hotels/api/v1/hotels/" + hotel.getId() + "/image");
            }
            return ResponseEntity.status(HttpStatus.OK).body(hotels);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseHotel> createHotel(@RequestBody ResponseHotel hotel) {
        ResponseHotel savedHotel = hotelService.saveHotel(hotel);
        if(savedHotel == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
    }

    @GetMapping("/hotels/{id}/image")
    public ResponseEntity<byte[]> getHotelImage(@PathVariable Long id) {
        ResponseHotel hotelOptional = hotelService.getHotelById(id);
        if (hotelOptional != null && hotelOptional.getImageData() != null) {
            byte[] imageData = hotelOptional.getImageData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<List<ResponseHotel>> getHotelsForOwner(@PathVariable long id) {
        List<ResponseHotel> outHotels = hotelService.getHotelsByOwner(id);
        if (outHotels == null || outHotels.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        for (ResponseHotel hotel : outHotels) {
            hotel.setImageData(null);
            hotel.setImageLink("http://localhost:8222/hotels/api/v1/hotel/" + hotel.getId() + "/image");
        }
        return new ResponseEntity<>(outHotels, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseHotel> updateHotel(@RequestBody ResponseHotel hotel) {
        ResponseHotel updatedHotel = hotelService.updateHotel(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable long id) {
        return new ResponseEntity<>(hotelService.deleteHotel(id), HttpStatus.OK);
    }

}
