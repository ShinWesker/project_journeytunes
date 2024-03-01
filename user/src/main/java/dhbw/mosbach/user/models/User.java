package dhbw.mosbach.user.models;

import dhbw.mosbach.user.cryptography.HashConfig;
import dhbw.mosbach.user.cryptography.SHA;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;
    private String email;
    private boolean isVerified;

    public User() {

    }

    public User(String name, String password, String email, boolean isVerified) {
        this.name = name;
        this.password = new SHA().hash(password, HashConfig.INSTANCE.getKey());
        this.email = email;
        this.isVerified = isVerified;
    }
}
