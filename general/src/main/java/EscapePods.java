// www.google.com/foobar => main.java.escape_pods
// problem_statement: resources/escape_pods.txt
package main.java;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import java.lang.Math;

/**
 * Created by nvijayv on 09/07/17.
 */
public class EscapePods {
    public static int answer(int[] entrances, int[] exits, int[][] path) {
        // This is a max-flow problem.
        // Adding dummy nodes s and t in the graph
        // s is connected to all entrances
        // t is connected to all exits

        int MAX_BUNNIES = 2000000;
        int N = path.length + 2;
        int s = N-2, t = N-1;
        Graph graph = new Graph(N, s, t);

        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[i].length; j++) {
                if (path[i][j] > 0) {
                    graph.addEdge(i, j, path[i][j]);
                }
            }
        }

        // Adding edge from s (N-2) to entrances
        for (int i = 0; i < entrances.length; i++) {
            graph.addEdge(s, entrances[i], MAX_BUNNIES);
        }
        // Adding edge from exits to t (N-1)
        for (int i = 0; i < exits.length; i++) {
            graph.addEdge(exits[i], t, MAX_BUNNIES);
        }

        return graph.getMaxFlowDinitz();
    }

    // Test
    public static void main(String[] args) {
        int[] entrances = {0, 1};
        int[] exits = {4, 5};
        int[][] path = {
                {0, 0, 4, 6, 0, 0},
                {0, 0, 5, 2, 0, 0},
                {0, 0, 0, 0, 4, 4},
                {0, 0, 0, 0, 6, 6},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
        System.out.println(answer(entrances, exits, path));
    }
}

class Edge {
    int start, end;
    int capacity;
    int rev;    // index of the reverse edge (end->start) in Graph.adjList[end]

    Edge(int _st, int _en, int _cap, int _rev) {
        start = _st;
        end = _en;
        capacity = _cap;
        rev = _rev;
    }
}

class Graph {
    int numNodes;
    int s, t;
    List<List<Edge>> adjList;
    int[] level = null;

    Graph(int N, int _s, int _t) {
        numNodes = N;
        adjList = new ArrayList<List<Edge>>(numNodes);
        level = new int[N];
        s = _s;
        t = _t;
    }

    public void addEdge(int start, int end, int capacity) {
        while (adjList.size() < Math.max(start, end) + 1) {
            adjList.add(new ArrayList<Edge>());
        }
        Edge forwardEdge = new Edge(start, end, capacity, adjList.get(end).size());
        Edge backEdge = new Edge(end, start, 0, adjList.get(start).size());
        adjList.get(start).add(forwardEdge);
        adjList.get(end).add(backEdge);
    }

    public int getMaxFlowDinitz() {
        int maxFlow = 0;
        while (bfs(s, t)) {
            int flow;
             while ((flow = sendFlow(s, t)) > 0) {
                 maxFlow += flow;
             }
        }
        return maxFlow;
    }

    private boolean bfs(int s, int t) {
        // Check if t is reachable from s, also construct the Level Graph meanwhile
        boolean sinkReachable = false;
        for (int i = 0; i < numNodes; i++) {
            level[i] = -1;
        }

        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        level[s] = 0;
        while (!queue.isEmpty()) {
            int node = queue.remove();
            for (Edge e: adjList.get(node)) {
                if (e.capacity == 0) {
                    continue;
                }
                if (level[e.end] < 0) {
                    level[e.end] = level[node] + 1;
                    queue.add(e.end);
                    if (e.end == t) {
                        sinkReachable = true;
                    }
                }
            }

        }
        return sinkReachable;
    }

    private int sendFlow(int s, int t) {
        Edge parent[] = new Edge[numNodes];
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        parent[s] = null;
        while (!queue.isEmpty() && parent[t] == null) {
            int node = queue.remove();
            for (Edge e: adjList.get(node)) {
                if (e.capacity == 0 || level[e.start]+1 != level[e.end]) {
                    continue;
                }
                if (parent[e.end] == null) {
                    parent[e.end] = e;
                    queue.add(e.end);
                    if (e.end == t) {
                        break;
                    }
                }
            }
        }

        int flow = 0;
        if (parent[t] != null) {
            flow = Integer.MAX_VALUE;
            int currNode = t;
            while (currNode != s) {
                Edge e = parent[currNode];
                flow = Math.min(flow, e.capacity);
                currNode = e.start;
            }
            if (flow > 0) {
                currNode = t;
                while (currNode != s) {
                    Edge e = parent[currNode];
                    sendFlow(e, flow);
                    currNode = e.start;
                }
            }
        }
        return flow;
    }

    private void sendFlow(Edge e, int flow) {
        e.capacity -= flow;
        adjList.get(e.end).get(e.rev).capacity += flow;
    }
}