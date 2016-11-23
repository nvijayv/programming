import java.util.LinkedList;
import java.util.List;

/**
 * Created by nvijayv on 11/23/16.
 * Problem: https://leetcode.com/problems/majority-element-ii/
 */
public class ThreeMajority {

    int zeroCounts = 0;

    public List<Integer> majorityElement(int[] nums) {
        List<Integer> kMajorityElementsIndices = new LinkedList<Integer>();
        List<ElementCount> kMajorityCandidates = new LinkedList<ElementCount>();
        for (final Integer num : nums) {
            updateCount(kMajorityCandidates, 3, num);
        }

        for (final ElementCount candCount : kMajorityCandidates) {
            if (candCount.count <= 0) {
                continue;
            }
            if (getCount(nums, candCount.element) > nums.length / 3) {
                kMajorityElementsIndices.add(candCount.element);
            }
        }
        return kMajorityElementsIndices;
    }

    private void updateCount(final List<ElementCount> kMajorityCandidates, final int K, final Integer num) {
        for (final ElementCount kmEleCount: kMajorityCandidates) {
            if (kmEleCount.element.equals(num)) {
                kmEleCount.count += 1;
                return;
            }
        }
        if (kMajorityCandidates.size() < K-1) {
            kMajorityCandidates.add(new ElementCount(num, 1));
        } else {
            // reduce counts of all other kMajorityCandidates
            int newZeroCounts = 0;
            for (final ElementCount kmEleCount: kMajorityCandidates) {
                if (zeroCounts == 0) {
                    kmEleCount.count--;
                    if (kmEleCount.count == 0) {
                        newZeroCounts++;
                    }
                } else if (zeroCounts > 0 && kmEleCount.count == 0) {
                    kmEleCount.element = num;
                    kmEleCount.count = 1;
                    zeroCounts--;
                    return;
                }
            }
            zeroCounts = newZeroCounts;
        }
    }

    private int getCount(int[] numbers, int candidate) {
        int count = 0;
        for (final Integer num: numbers) {
            if (num == candidate) {
                count++;
            }
        }
        return count;
    }
}

class ElementCount {
    Integer element;
    int count;

    ElementCount(Integer _ele, int _cnt) {
        element = _ele;
        count = _cnt;
    }
}
