package dhbw.mosbach.user.services;

import dhbw.mosbach.user.models.Token;
import dhbw.mosbach.user.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TokenService {

    private TokenRepository repository;

    public boolean existsByTokenValue(String value) {
        return repository.existsByValue(value);
    }

    public void saveToken(Token token) {
        repository.save(token);
    }

    public Token getTokenByValue(String value) {
        return repository.findByValue(value);
    }

    public void deleteToken(long id) {
        repository.deleteById(id);
    }

}