package polsl.tsp;

public class GraphUtils {
    // Generate a sample graph (adjacency matrix)
    public static int[][] generateSampleGraph() {
        return new int[][]{
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
    }

    // Compute expected cost for a known graph (brute-force for testing)
    public static int calculateExpectedCost() {
        // Known solution for the above graph
        return 80; // Example value, adjust based on the graph
    }

    // Validate a TSP path
    public static boolean validatePath(int[] path, int numNodes) {
        if (path.length != numNodes + 1) {
            return false;
        }

        if (path[0] != path[path.length - 1]) {
            return false;
        }

        boolean[] visited = new boolean[numNodes];
        // Skip the last element (which is same as first)
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
}
