package polsl.tsp.simple;

import java.util.Arrays;

/**
 * PROMPT: Create a TSP solution in Java
 */
public class SimpleMsCopilotSolution {
    static int n = 4;
    static int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
    };
    static int[] minTour;
    static int minCost = Integer.MAX_VALUE;

    public static void main(String[] args) {
        int[] path = new int[n + 1];
        boolean[] visited = new boolean[n];
        Arrays.fill(visited, false);
        path[0] = 0;
        visited[0] = true;
        solveTSP(path, visited, 1, 0);
        System.out.println("Minimum cost: " + minCost);
        System.out.println("Tour: " + Arrays.toString(minTour));
    }

    static void solveTSP(int[] path, boolean[] visited, int pos, int cost) {
        if (pos == n) {
            cost += graph[path[pos - 1]][path[0]];
            if (cost < minCost) {
                minCost = cost;
                minTour = path.clone();
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i] && graph[path[pos - 1]][i] != 0) {
                visited[i] = true;
                path[pos] = i;
                solveTSP(path, visited, pos + 1, cost + graph[path[pos - 1]][i]);
                visited[i] = false;
            }
        }
    }
}
