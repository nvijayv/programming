package nv.epi.primitivetypes;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by nvijayv on 9/2/16.
 */
@RunWith(JUnitQuickcheck.class)
public class StringProperties {

    @Property public void concatenationLength(String s1, String s2) {
        assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }
}