package dhbw.mosbach.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dhbw.mosbach.hotel.models.ContactData;

public interface ContactDataRepository extends JpaRepository<ContactData, Long> {
    boolean existsByEmailAndPhoneNumber(String email, long phoneNumber);
}
