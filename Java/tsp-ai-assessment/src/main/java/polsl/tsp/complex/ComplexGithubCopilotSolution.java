package polsl.tsp.complex;

import java.util.Arrays;

public class ComplexGithubCopilotSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        int[] path = new int[n + 1];
        Arrays.fill(path, -1);
        int cost = tspUtil(graph, visited, 0, n, 1, 0, path);
        return new TspResult(path, cost);
    }

    private int tspUtil(int[][] graph, boolean[] visited, int currPos, int n, int count, int cost, int[] path) {
        if (count == n && graph[currPos][0] > 0) {
            path[count] = currPos;
            path[count + 1] = 0;
            return cost + graph[currPos][0];
        }

        int minCost = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            if (!visited[i] && graph[currPos][i] > 0) {
                visited[i] = true;
                path[count] = currPos;
                int newCost = tspUtil(graph, visited, i, n, count + 1, cost + graph[currPos][i], path);
                minCost = Math.min(minCost, newCost);
                visited[i] = false;
            }
        }

        return minCost;
    }
}
