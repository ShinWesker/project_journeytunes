package dhbw.mosbach.user.cryptography;

public interface IHash {

    String hash(String cipher, String secretKey);
}
