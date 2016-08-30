package nv.epi.primitivetypes;

/**
 * Created by nvijayv on 8/28/16.
 */
public class ReversingBits {

    static long reverseBits(long num) {
        for (int i = 0; i < 64; i++) {
            num = SwappingBits.swapBits(num, i, 63 - i);
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(reverseBits(-2L));
    }
}
