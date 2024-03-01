package dhbw.mosbach.hotel.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import dhbw.mosbach.hotel.models.Hotel;
public class HotelSpecifications {

    public static Specification<Hotel> hasRegion(String region) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(region)) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("locationId").get("region"), region);
        };
    }

    public static Specification<Hotel> pricePerNightLessThanEqual(double pricePerNight) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("pricePerNight"), pricePerNight);
    }

    public static Specification<Hotel> hasStars(int stars) {
        return (root, query, cb) -> cb.equal(root.get("stars"), stars);
    }

    public static Specification<Hotel> nameContains(String name) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(name)) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}