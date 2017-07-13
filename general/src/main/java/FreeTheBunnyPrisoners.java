package main.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nvijayv on 12/07/17.
 */
public class FreeTheBunnyPrisoners {

    static final int MAX = 10;

    static final int[][] nCk = new int[MAX][MAX];

    static {
        for (int i = 1; i < MAX; i++) {
            nCk[i][0] = 1;
            nCk[i][1] = i;
            nCk[i][i] = 1;
        }
        for (int i = 3; i < MAX; i++) {
            for (int j = 2; j < i; j++) {
                nCk[i][j] = nCk[i-1][j-1] + nCk[i-1][j];
            }
        }
    }

    public static int[][] answer(int num_buns, int num_required) {
        int perKeyFrequency = num_buns - num_required + 1;
        int numKeys = nCk[num_buns][perKeyFrequency];
        int numKeysPerBunny = perKeyFrequency * numKeys / num_buns;
        int[][] keysDistribution = new int[num_buns][numKeysPerBunny];

        List<List<Integer>> bunnySelections = createBunnySelections(num_buns, perKeyFrequency);
        int[] indicesToFill = new int[num_buns];
        int key = 0;
        for (List<Integer> selection: bunnySelections) {
            for (Integer bunny: selection) {
                keysDistribution[bunny][ indicesToFill[bunny] ] = key;
                indicesToFill[bunny]++;
            }
            key++;
        }
        return keysDistribution;
    }

    private static List<List<Integer>> createBunnySelections(int n, int k) {
        List<List<Integer>> allSelections = new ArrayList<>();
        List<Integer> runningSelection = new ArrayList<>(k);
        select(allSelections, runningSelection, n, k, 0);
        return allSelections;
    }

    private static void select(List<List<Integer>> allSelections, List<Integer> runningSelection, int n, int k, int curr) {
        List<Integer> copyRunningSelection = new ArrayList<>(runningSelection);

        // include curr
        runningSelection.add(curr);
        if (runningSelection.size() == k) {
            allSelections.add(runningSelection);
        } else if (curr + 1 < n) {
            select(allSelections, runningSelection, n, k, curr + 1);
        }
        // ignore curr
        if (curr + 1 < n) {
            select(allSelections, copyRunningSelection, n, k, curr + 1);
        }
    }

    public static void main(String[] args) {
        answer(5, 2);
    }
}