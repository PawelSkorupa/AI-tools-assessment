package polsl.tsp.complex;

import java.util.Arrays;

public class ComplexTabnineSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        if (graph == null || graph.length == 0 || graph[0].length != graph.length) {
            throw new IllegalArgumentException("Invalid graph input");
        }

        int n = graph.length;
        int[] path = new int[n + 1];
        boolean[] visited = new boolean[n];
        int totalCost = 0;

        // Start from the first city (index 0)
        int currentCity = 0;
        path[0] = currentCity;
        visited[currentCity] = true;

        for (int i = 1; i < n; i++) {
            int nearestCity = findNearestUnvisitedCity(currentCity, graph, visited);
            path[i] = nearestCity;
            totalCost += graph[currentCity][nearestCity];
            visited[nearestCity] = true;
            currentCity = nearestCity;
        }

        // Return to the starting city
        path[n] = 0;
        totalCost += graph[currentCity][0];

        return new TspResult(path, totalCost);
    }

    private int findNearestUnvisitedCity(int currentCity, int[][] graph, boolean[] visited) {
        int nearestCity = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < graph.length; i++) {
            if (!visited[i] && graph[currentCity][i] < minDistance) {
                minDistance = graph[currentCity][i];
                nearestCity = i;
            }
        }

        return nearestCity;
    }
}