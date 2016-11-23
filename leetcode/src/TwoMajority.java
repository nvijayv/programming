/**
 * Created by nvijayv on 11/23/16.
 * Problem: https://leetcode.com/problems/majority-element/
 */
public class TwoMajority {

    // Assumes that 2-majority element exists in input
    public int majorityElement(int[] nums) {
        int length = nums.length;
        int candidate = nums[0];
        int candidateCountApprox = 1;
        for (int i = 1; i < length; i++) {
            if (nums[i] == candidate) {
                candidateCountApprox++;
            } else {
                candidateCountApprox--;
                if (candidateCountApprox < 0) {
                    candidate = nums[i];
                    candidateCountApprox = 1;
                }
            }
        }
        if (candidateCountApprox > 0) {
            int count = 0;
            for (int idx = 0; idx < length; idx++) {
                if (nums[idx] == candidate) {
                    count++;
                }
            }
            if (count >= length/2 + 1) {
                return candidate;
            }
        }
        return -1;
    }
}
