package polsl.tsp;

import polsl.tsp.complex.TspResult;

public class GraphUtils {

    /**
     * Provides a sample graph along with the known optimal TSP solution cost.
     */
    public static SampleTspData getSampleGraph4x4() {
        int[][] graph = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        int[] optimalPath = {0, 1, 3, 2, 0};
        int cost = calculatePathCost(graph, optimalPath);

        return new SampleTspData(graph, new TspResult(optimalPath, cost));
    }

    public static SampleTspData getSampleGraph10x10() {
        int[][] graph = {
                {0, 29, 20, 21, 16, 31, 100, 12, 4, 31},
                {29, 0, 15, 29, 28, 40, 72, 21, 29, 41},
                {20, 15, 0, 15, 14, 25, 81, 9, 23, 27},
                {21, 29, 15, 0, 4, 12, 92, 12, 25, 13},
                {16, 28, 14, 4, 0, 16, 94, 9, 20, 16},
                {31, 40, 25, 12, 16, 0, 95, 24, 36, 3},
                {100, 72, 81, 92, 94, 95, 0, 90, 101, 99},
                {12, 21, 9, 12, 9, 24, 90, 0, 15, 25},
                {4, 29, 23, 25, 20, 36, 101, 15, 0, 35},
                {31, 41, 27, 13, 16, 3, 99, 25, 35, 0}
        };

        int[] optimalPath = {0, 4, 3, 9, 5, 6, 1, 2, 7, 8, 0};
        int cost = calculatePathCost(graph, optimalPath);

        return new SampleTspData(graph, new TspResult(optimalPath, cost));
    }

    public static SampleTspData getSampleGraph15x15() {
        int[][] graph = {
                {0, 2, 9, 10, 1, 5, 7, 12, 14, 6, 3, 8, 15, 4, 11},
                {2, 0, 8, 9, 6, 7, 3, 10, 13, 4, 5, 9, 11, 8, 12},
                {9, 8, 0, 4, 5, 6, 2, 7, 10, 3, 8, 11, 13, 9, 14},
                {10, 9, 4, 0, 6, 3, 5, 4, 9, 2, 6, 8, 12, 7, 10},
                {1, 6, 5, 6, 0, 2, 4, 9, 11, 5, 2, 7, 10, 6, 9},
                {5, 7, 6, 3, 2, 0, 3, 5, 8, 1, 4, 6, 9, 5, 7},
                {7, 3, 2, 5, 4, 3, 0, 4, 7, 3, 5, 8, 10, 6, 9},
                {12, 10, 7, 4, 9, 5, 4, 0, 3, 6, 7, 10, 12, 8, 11},
                {14, 13, 10, 9, 11, 8, 7, 3, 0, 8, 10, 11, 14, 10, 13},
                {6, 4, 3, 2, 5, 1, 3, 6, 8, 0, 3, 5, 7, 4, 6},
                {3, 5, 8, 6, 2, 4, 5, 7, 10, 3, 0, 4, 6, 2, 5},
                {8, 9, 11, 8, 7, 6, 8, 10, 11, 5, 4, 0, 3, 5, 7},
                {15, 11, 13, 12, 10, 9, 10, 12, 14, 7, 6, 3, 0, 8, 10},
                {4, 8, 9, 7, 6, 5, 6, 8, 10, 4, 2, 5, 8, 0, 3},
                {11, 12, 14, 10, 9, 7, 9, 11, 13, 6, 5, 7, 10, 3, 0}
        };

        int[] optimalPath = {0, 4, 5, 9, 10, 13, 14, 12, 11, 6, 2, 3, 7, 8, 1, 0}; // might be wrong
        int cost = calculatePathCost(graph, optimalPath);

        return new SampleTspData(graph, new TspResult(optimalPath, cost));
    }

    public static SampleTspData getSampleAsymmetricalGraph() {
        int[][] graph = {
                { 0, 12, 10, 19 },
                { 8,  0, 20,  6 },
                {14, 18,  0, 16 },
                {11,  7, 12,  0 }
        };

        int[] optimalPath = {0, 2, 3, 1, 0};
        int cost = 41;

        return new SampleTspData(graph, new TspResult(optimalPath, cost));
    }

    /**
     * Validates if the given path is a valid TSP solution for the graph.
     */
    public static boolean validatePath(int[] path, int numNodes) {
        if (path.length != numNodes + 1) {
            return false;
        }

        if (path[0] != path[path.length - 1]) {
            return false;
        }

        boolean[] visited = new boolean[numNodes];
        for (int i = 0; i < path.length - 1; i++) {
            int node = path[i];
            if (node < 0 || node >= numNodes || visited[node]) {
                return false;
            }
            visited[node] = true;
        }

        return true;
    }

    // Calculate path cost
    public static int calculatePathCost(int[][] graph, int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph[path[i]][path[i + 1]];
        }
        return cost;
    }

    /**
     * Wrapper for returning both the graph and the known solution.
     */
    public static class SampleTspData {
        private final int[][] graph;
        private final TspResult expectedResult;

        public SampleTspData(int[][] graph, TspResult expectedResult) {
            this.graph = graph;
            this.expectedResult = expectedResult;
        }

        public int[][] getGraph() {
            return graph;
        }

        public TspResult getExpectedResult() {
            return expectedResult;
        }
    }
}
