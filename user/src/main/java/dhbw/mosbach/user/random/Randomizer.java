package dhbw.mosbach.user.random;

public enum Randomizer {
    INSTANCE;
    private final MersenneTwister mersenneTwister = new MersenneTwister(System.currentTimeMillis());

    public MersenneTwister getMersenneTwister() {
        return mersenneTwister;
    }
}
