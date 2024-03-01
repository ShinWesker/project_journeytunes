package dhbw.mosbach.user.cryptography;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SHA implements IHash {

    @Override
    public String hash(String plainMessage, String secretKey) {
        return Hashing.sha256()
                .hashString(plainMessage, StandardCharsets.UTF_8)
                .toString();
    }
}
