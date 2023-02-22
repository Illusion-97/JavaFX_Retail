package dwn.jfx.retail.tools;

import java.util.Random;

public abstract class Randomizer {
    private static final Random random = new Random(new Random().nextLong());

    public static double Randomize(double number) {
        return number * (1 - ((50 - random.nextInt(101)) / 100D));
    }
}
