package nv.epi.primitivetypes;

/**
 * Created by nvijayv on 8/27/16.
 * Parity of integer n: 1 if no. of set bits in n is Odd, 0 otherwise
 */
public class Parity {

    static int SIXTEEN = 16;
    static int MAX_16_BIT_INTEGER = 0xFFFF;
    static long[] basicParities = new long[MAX_16_BIT_INTEGER + 1];

    static void init() {
        for (int i = 0; i <= MAX_16_BIT_INTEGER; i++) {
            basicParities[i] = getParityQuickly((long)i);
        }
    }

    // Time Complexity: O(no. of bits)
    static long getParity(long num) {
        long parity = 0;
        while (num > 0) {
            parity ^= (num & 1);
            num = num >> 1L;
        }
        return parity;
    }

    // Time complexity: O(no. of set bits)
    static long getParityQuickly(long num) {
        long parity = 0;
        while (num > 0) {
            parity ^= 1;
            num &= (num - 1);
        }
        return parity;
    }

    // After memoisation, O(1)
    static long getParityUsingMemoised(final String numStr) {
        Long unsignedNum = Long.parseUnsignedLong(numStr);
        long parity = 0;
        long mask = MAX_16_BIT_INTEGER;
        for (int i = 0; i < 4; i++) {
            parity ^= basicParities[(int) (unsignedNum & mask)];
            unsignedNum >>= SIXTEEN;
        }
        return parity;
    }

    // O(1)
    static long getParityVeryQuickly(long num) {
        num ^= num >> 32;
        num ^= num >> 16;
        num ^= num >> 8;
        num ^= num >> 4;
        num ^= num >> 2;
        num ^= num >> 1;
        return num & 1;
    }

    public static void main(String[] args) {
        long num = 65536;
        System.out.println("parity(" + num + "): " + getParity(num));
        System.out.println("parity(" + num + "): " + getParityQuickly(num));
        init();
        System.out.println("parity(" + num + "): " + getParityUsingMemoised(String.valueOf(num)));
        System.out.println("parity(" + num + "): " + getParityVeryQuickly(num));
    }
}
