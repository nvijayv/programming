package main.java;

/**
 * Created by nvijayv on 11/21/16.
 */
public class LocalMinima1D {

    // Expects all integers to be distinct
    public int getLocalMinima(int[] numbers) {
        int length = numbers.length;
        int start = 0, end = length - 1;
        while (start < end-1) {
            int mid = start + (end - start)/2;
            if (numbers[mid] < numbers[mid-1] && numbers[mid] < numbers[mid+1]) {
                return mid;
            } else if (numbers[mid-1] < numbers[mid]) {
                end = mid-1;
            } else {
                start = mid+1;
            }
        }
        if (start < end) {
            if (numbers[start] < numbers[end]) {
                return start;
            }
            return end;
        }
        return start;
    }

    public static void main(String[] args) {
        LocalMinima1D lm = new LocalMinima1D();

        assert lm.getLocalMinima(new int[] {1}) == 0;
        assert lm.getLocalMinima(new int[] {3, 2}) == 1;
        assert lm.getLocalMinima(new int[] {1, 0, 2}) == 1;
        int idx = lm.getLocalMinima(new int[] {10, 9, 8, 4, 2, 1, 5, 17}); assert idx == 3 || idx == 5;
    }
}
