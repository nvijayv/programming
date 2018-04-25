package maximal_tree_diameter;

import java.util.*;

/**
 * Created by nvijayv on 4/17/17.
 */
public class MaximalTreeDiameterFast {

    private static ArrayList<List<Integer>> graph;
    private static int[] diaDown;

    private static void init(int numVertices) {
        graph = new ArrayList<List<Integer>>(numVertices);
        for (int idx = 0; idx < numVertices; idx++) {
            graph.add(new LinkedList<Integer>());
        }
        diaDown = new int[numVertices];
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        init(n);

        for (int idx = 0; idx < n-1; idx++) {
            int u = in.nextInt();
            int v = in.nextInt();
            graph.get(u-1).add(v-1);
            graph.get(v-1).add(u-1);
        }
    }
}
