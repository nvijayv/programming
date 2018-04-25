package nv.epi.primitivetypes;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by nvijayv on 8/30/16.
 */
@RunWith(JUnitQuickcheck.class)
public class ParityTest {

    @Property
    public void testGetParity(long num) throws Exception {
        assertEquals(Parity.getParity(num), getBruteParity(num));
    }

    private static int getBruteParity(long num) {
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