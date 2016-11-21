package main.java;

import java.util.Random;

/**
 * Created by nvijayv on 11/15/16.
 */
public class KthElement {

    private static Random rand = new Random();

    public int getKthElement(int[] numbers, int start, int end, int K) {
        if (numbers == null) {
            throw new RuntimeException("getKthElement received null array");
        }
        int len = end - start + 1;
        if (len == 0) {
            throw new RuntimeException("getKthElement received empty array");
        }
        if (start > end) {
            throw new RuntimeException("invalid start & end params: " + start + ", " + end);
        }
        if (K < 0 || K >= len) {
            throw new RuntimeException("Invalid K: either -ve or more than size of array");
        }
        if (len == 1) {
            return numbers[start];
        }

//        final int pivotIndex = selectRandomPivotIndex(numbers, start, end);
        final int pivotIndex = selectMedianOfMediansAsPivotIndex(numbers, start, end);
        int pivotActualIndex = partition(numbers, start, end, pivotIndex);

        if (pivotActualIndex == K + start) {
            return numbers[K + start];
        } else if (pivotActualIndex > K + start) {
            return getKthElement(numbers, start, pivotActualIndex - 1, K);
        } else {
            return getKthElement(numbers, pivotActualIndex + 1, end, K + start - pivotActualIndex - 1);
        }
    }

    private int selectRandomPivotIndex(final int[] numbers, final int start, final int end) {
        return start + rand.nextInt(end - start + 1);
    }

    private int selectMedianOfMediansAsPivotIndex(final int[] numbers, final int start, final int end) {
        final int len = end - start + 1;
        int medianOfMedians = getMedianOfMedians(numbers, start, len);
        for (int idx = start; idx < start + len; idx++) {
            if (numbers[idx] == medianOfMedians) {
                return idx;
            }
        }
        throw new RuntimeException("Oh My Turing! Median not found");
    }

    public int getMedianOfMedians(final int[] numbers, final int start, final int len) {
        if (len <= 5) {
            return numbers[getMedianIndex(numbers, start, len)];
        }
        int numMedians = len/5 + (len % 5 > 0 ? 1 : 0);
        int[] arrayOfMedians = new int[numMedians];
        for (int k = 0; 5*k < len; k++) {
            final int medIdx = getMedianIndex(numbers, start + 5*k, Math.min(5, len-5*k));
            arrayOfMedians[k] = numbers[medIdx];
        }
        return getMedianOfMedians(arrayOfMedians, 0, numMedians);
    }

    private int getMedianIndex(final int[] numbers, final int start, final int len) {
        int minIndex = start, secondMinIndex = start, thirdMinIndex = start;
        for (int index = start + 1; index < start + len; index++) {
            if (numbers[index] < numbers[minIndex]) {
                thirdMinIndex = secondMinIndex;
                secondMinIndex = minIndex;
                minIndex = index;
            } else if (numbers[index] < numbers[secondMinIndex]) {
                thirdMinIndex = secondMinIndex;
                secondMinIndex = index;
            } else if (numbers[index] < numbers[thirdMinIndex]) {
                thirdMinIndex = index;
            } else if (minIndex == secondMinIndex) {
                secondMinIndex = thirdMinIndex = index;
            } else if (secondMinIndex == thirdMinIndex) {
                thirdMinIndex = index;
            }
        }
        switch (len) {
            case 1:
            case 2:
                return minIndex;
            case 3:
            case 4:
                return secondMinIndex;
            case 5:
                return thirdMinIndex;
            default:
                throw new RuntimeException("getMedianIndex got len < 1 or len > 5");
        }
    }

    // Modifies :numbers: while partitioning around :pivot:
    public int partition(final int[] numbers, final int start, final int end, final int pivotIndex) {
        if (numbers != null) {
            int len = end - start + 1;
            if (len < 2) {
                return start;
            }
            final int pivot = numbers[pivotIndex];
            swap(numbers, start, pivotIndex);
            State state = State.BIG;
            if (numbers[end] <= pivot) {
                state = State.SMALL;
            }

            int partitionIndex = end;
            for (int idx = end; idx > start; idx--) {
                if (numbers[idx] <= pivot && state == State.BIG) {
                    partitionIndex = idx;
                    state = State.SMALL;
                } else if (numbers[idx] > pivot && state == State.SMALL) {
                    swap(numbers, idx, partitionIndex);
                    partitionIndex--;
                }
            }
            if (state == State.SMALL) {
                swap(numbers, start, partitionIndex);
                return partitionIndex;
            }
            return start;
        }
        return -1;
    }

    private enum State {
        SMALL,
        BIG
    };

    private void swap(final int[] numbers, final int idx, final int jdx) {
        final int temp = numbers[idx];
        numbers[idx] = numbers[jdx];
        numbers[jdx] = temp;
    }

    public static void main(String[] args) {
        System.out.println("Test 1");
        for (int idx = 0; idx < 7; idx++) {
            int[] numbers = {3, 4, 2, 0, 6, -1, 8};     // [-1, 0, 2, 3, 4, 6, 8]
            KthElement kthElement = new KthElement();
            System.out.println(idx + "th element: " + kthElement.getKthElement(numbers, 0, numbers.length - 1, idx));
        }
        System.out.println("--------------------------\n");

        System.out.println("Test 2");
        for (int idx = 0; idx < 7; idx++) {
            int[] numbers = {23, 14, 12, 11, 10, 9, 8};     // [8, 9, 10, 11, 12, 14, 23]
            KthElement kthElement = new KthElement();
            System.out.println(idx + "th element: " + kthElement.getKthElement(numbers, 0, numbers.length - 1, idx));
        }
        System.out.println("--------------------------\n");

        System.out.println("Test 3");
        for (int idx = 0; idx < 7; idx++) {
            int[] numbers = {2, 1, 0, 1, 2, 8, 14, 6, 14, 8};     // [0, 1, 1, 2, 2, 6, 8, 8, 14, 14]
            KthElement kthElement = new KthElement();
            System.out.println(idx + "th element: " + kthElement.getKthElement(numbers, 0, numbers.length - 1, idx));
        }
        System.out.println("--------------------------\n");

//        int[] numbers = {3, 4, 12, 10, 16, -1, -8};
        int[] numbers = {23, 14, 12, 11, 10, 9, 8};     // [8, 9, 10, 11, 12, 14, 23]
        System.out.println("Median: " + new KthElement().getMedianOfMedians(numbers, 0, 6));
    }
}
