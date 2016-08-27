package nv.epi.primitivetypes;

/**
 * Created by nvijayv on 8/27/16.
 */
public class BitSwapping {

    static long swapBits(long num, int i, int j) {
        if (((num & (1 << i)) >> i) != ((num & (1 << j)) >> j)) {
            num ^= (1 << i) | (1 << j);
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(swapBits(2, 0, 1));
        System.out.println(swapBits(1, 0, 2));
        System.out.println(swapBits(3, 0, 1));
    }
}
