package dhbw.mosbach.user.services;

import dhbw.mosbach.user.models.User;
import dhbw.mosbach.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository repository;

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public User getUserById(long id) {
        return repository.findById(id).orElse(null);
    }

    public User getUserByName(String name) {
        return repository.findByName(name);
    }


}