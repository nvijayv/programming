// https://www.hackerrank.com/contests/hourrank-19/challenges/maximal-tree-diameter

package maximal_tree_diameter;

import java.util.*;

class MaximalTreeDiameter {

    private static List<List<Edge>> graph;

    private static int[] diaDown;
    private static ArrayList<List<PathNeighbor>> topPathsDown;

    private static Map<Edge, List<PathNeighbor>> topPathsUpExcluding;

    private static Edge[] parent;

    private static List<Edge> edgesOrder;

    private static void init(int numVertices) {
        graph = new ArrayList<List<Edge>>(numVertices);
        for (int idx = 0; idx < numVertices; idx++) {
            graph.add(new LinkedList<Edge>());
        }

        diaDown = new int[numVertices];
        topPathsDown = new ArrayList<List<PathNeighbor>>(numVertices);
        for (int idx = 0; idx < numVertices; idx++) {
            topPathsDown.add(new LinkedList<PathNeighbor>());
        }

        topPathsUpExcluding = new HashMap<Edge, List<PathNeighbor>>();

        parent = new Edge[numVertices];

        edgesOrder = new LinkedList<Edge>();
    }

    private static void dfsDown(int u, int p) {
        PathNeighbor maxLenPN = null, secondMaxLenPN = null, thirdMaxLenPN = null;
        for (Edge e: graph.get(u)) {
            int v = e.v;
            if (v == p) {
                continue;
            }
            edgesOrder.add(e);
            parent[v] = e;
            dfsDown(v, u);
            diaDown[u] = Math.max(diaDown[u], diaDown[v]);
            PathNeighbor pn;
            if (topPathsDown.get(v).isEmpty()) {
                pn = new PathNeighbor(e, 1);
            } else {
                pn = new PathNeighbor(e, 1 + topPathsDown.get(v).get(0).length);
            }
            if (maxLenPN == null || maxLenPN.length < pn.length) {
                if (maxLenPN != null) {
                    if (secondMaxLenPN != null) {
                        thirdMaxLenPN = secondMaxLenPN;
                    }
                    secondMaxLenPN = maxLenPN;
                }
                maxLenPN = pn;
            } else if (secondMaxLenPN == null || secondMaxLenPN.length < pn.length) {
                if (secondMaxLenPN != null) {
                    thirdMaxLenPN = secondMaxLenPN;
                }
                secondMaxLenPN = pn;
            } else if (thirdMaxLenPN == null || thirdMaxLenPN.length < pn.length) {
                thirdMaxLenPN = pn;
            }
        }
        if (maxLenPN == null) {
            return;
        }

        topPathsDown.get(u).add(maxLenPN);
        if (secondMaxLenPN != null) {
            topPathsDown.get(u).add(secondMaxLenPN);
        }
        if (thirdMaxLenPN != null) {
            topPathsDown.get(u).add(thirdMaxLenPN);
        }

        int maxDiaThroughU = topPathsDown.get(u).get(0).length;
        if (topPathsDown.get(u).size() > 1) {
            maxDiaThroughU += topPathsDown.get(u).get(1).length;
        }
        diaDown[u] = Math.max(diaDown[u], maxDiaThroughU);
    }

    private static int repeatDfs() {
        int maxPotentialDia = 0;
        for (Edge e: edgesOrder) {
            int u = e.u;
            int v = e.v;
            PathNeighbor maxLenPN = null, secondMaxLenPN = null;
            if (parent[u] != null) {
                Edge parentEdge = parent[u];
                if (!topPathsUpExcluding.get(parentEdge).isEmpty()) {
                    PathNeighbor topPathNbr = topPathsUpExcluding.get(parentEdge).get(0);
                    maxLenPN = new PathNeighbor(topPathNbr.nbrEdge, 1 + topPathNbr.length);
                } else {
                    maxLenPN = new PathNeighbor(parentEdge, 1);
                }
            }
            List<PathNeighbor> pns = topPathsDown.get(u);
            for (int idx = 0; idx < 3 && idx < pns.size(); idx++) {
                if (pns.get(idx).nbrEdge.v != v) {
                    if (maxLenPN == null || maxLenPN.length < pns.get(idx).length) {
                        if (maxLenPN != null) {
                            secondMaxLenPN = maxLenPN;
                        }
                        maxLenPN = pns.get(idx);
                    } else if (secondMaxLenPN == null || secondMaxLenPN.length < pns.get(idx).length) {
                        secondMaxLenPN = pns.get(idx);
                    }
                }
            }

            int maxDiaThroughU = 0;
            topPathsUpExcluding.put(e, new ArrayList<PathNeighbor>());
            if (maxLenPN != null) {
                topPathsUpExcluding.get(e).add(maxLenPN);
                if (secondMaxLenPN != null) {
                    topPathsUpExcluding.get(e).add(secondMaxLenPN);
                }
                maxDiaThroughU = topPathsUpExcluding.get(e).get(0).length;
                if (topPathsUpExcluding.get(e).size() > 1) {
                    maxDiaThroughU += topPathsUpExcluding.get(e).get(1).length;
                }
            }
            maxPotentialDia = Math.max(maxPotentialDia, 1 + maxDiaThroughU + diaDown[v]);
        }

        return maxPotentialDia;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        init(n);
        for (int idx = 0; idx < n-1; idx++) {
            int u = in.nextInt();
            int v = in.nextInt();
            graph.get(u-1).add(new Edge(u-1, v-1));
            graph.get(v-1).add(new Edge(v-1, u-1));
        }
        parent[0] = null;
        dfsDown(0, -1);

        int maxPotentialDia = repeatDfs();
        System.out.println(maxPotentialDia);
    }
}

class PathNeighbor {
    Edge nbrEdge;
    int length;

    PathNeighbor(Edge _nbr, int _length) {
        nbrEdge = _nbr;
        length = _length;
    }
}

class Edge {
    int u;
    int v;

    Edge(int _u, int _v) {
        u = _u;
        v = _v;
    }
}