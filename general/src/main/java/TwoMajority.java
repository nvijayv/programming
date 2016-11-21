package main.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by nvijayv on 11/20/16.
 */
public class TwoMajority {

    public int getTwoMajorityElement(List<Integer> numbers) {
        int length = numbers.size();
        int candidateIdx = 0;
        int candidateCountApprox = 1;
        for (int i = 1; i < length; i++) {
            if (numbers.get(i).equals(numbers.get(candidateIdx))) {
                candidateCountApprox++;
            } else {
                candidateCountApprox--;
                if (candidateCountApprox < 0) {
                    candidateIdx = i;
                    candidateCountApprox = 1;
                }
            }
        }
        if (candidateCountApprox > 0) {
            int count = 0;
            int minCandidateIdx = candidateIdx;
            for (int idx = 0; idx < numbers.size(); idx++) {
                if (numbers.get(idx).equals(numbers.get(candidateIdx))) {
                    count++;
                    if (idx < minCandidateIdx) {
                        minCandidateIdx = idx;
                    }
                }
            }
            if (count >= length/2 + 1) {
                return minCandidateIdx;
            }
        }
        return -1;
    }

    // Make sure to pass -ea to JVM to enable assertion otherwise below assert statements won't work
    public static void main(String[] args) {
        TwoMajority tm = new TwoMajority();

        assert tm.getTwoMajorityElement(Collections.singletonList(1)) == 0;
        assert tm.getTwoMajorityElement(Arrays.asList(1, 2)) == -1;
        assert tm.getTwoMajorityElement(Arrays.asList(1, 1)) == 0;
        assert tm.getTwoMajorityElement(Arrays.asList(1, 2, 1)) == 0;
        assert tm.getTwoMajorityElement(Arrays.asList(3, 2, 1, 2)) == -1;
        assert tm.getTwoMajorityElement(Arrays.asList(5, 1, 1, 1, 2, 3, 1, 1)) == 1;
        assert tm.getTwoMajorityElement(Arrays.asList(5, 2, 3, 2, 2, 2, 5)) == 1;
    }
}
