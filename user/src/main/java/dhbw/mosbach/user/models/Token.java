package dhbw.mosbach.user.models;

import dhbw.mosbach.user.cryptography.HashConfig;
import dhbw.mosbach.user.cryptography.SHA;
import dhbw.mosbach.user.random.Randomizer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Token {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String value;
    public Token() {
        for (int i = 0; i < 70; i++) value = value + Randomizer.INSTANCE.getMersenneTwister().nextChar();
        value = new SHA().hash(value, HashConfig.INSTANCE.getKey());
    }
    public Token(long userId) {
        this.userId = userId;
        for (int i = 0; i < 70; i++) value = value + Randomizer.INSTANCE.getMersenneTwister().nextChar();
        value = new SHA().hash(value, HashConfig.INSTANCE.getKey());
    }
}
