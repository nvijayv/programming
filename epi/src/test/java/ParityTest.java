import net.java.quickcheck.QuickCheck;
import net.java.quickcheck.generator.iterable.Iterables;
import net.java.quickcheck.generator.support.IntegerGenerator;
import nv.epi.primitivetypes.Parity;
import org.junit.runner.RunWith;
import org.testng.annotations.Test;

/**
 * Created by nvijayv on 8/30/16.
 */
public class ParityTest {

    public static void getParityTest() {
        for (Integer num: Iterables.toIterable(new IntegerGenerator())) {
            assert Parity.getParity(num) == -1;
        }
    }

    private static int getBruteParity(int num) {
        int count = 1;
        while (num > 0) {
            if (num % 2 == 1) {
                count += 1;
            }
            num /= 2;
        }
        return count % 2;
    }
}
