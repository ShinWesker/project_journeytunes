package dhbw.mosbach.user.repositories;

import dhbw.mosbach.user.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByValue(String value);
    boolean existsByValue(String value);
}