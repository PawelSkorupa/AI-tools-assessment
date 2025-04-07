package polsl.tsp.complex;
import java.util.Arrays;

/**
 * Implementation of the Traveling Salesman Problem solver using the nearest neighbor algorithm.
 * This is a greedy approach that constructs a path by always visiting the nearest unvisited city.
 */
public class ComplexClaudeSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        if (graph == null || graph.length == 0) {
            throw new IllegalArgumentException("Graph cannot be null or empty");
        }

        int n = graph.length;

        // If only one city, return path with just that city
        if (n == 1) {
            return new TspResult(new int[]{0}, 0);
        }

        // Track visited cities
        boolean[] visited = new boolean[n];
        // Path will store the order of visited cities
        int[] path = new int[n + 1]; // +1 because we return to the starting city
        int totalCost = 0;

        // Start from city 0
        int currentCity = 0;
        path[0] = currentCity;
        visited[currentCity] = true;

        // Visit each city
        for (int i = 1; i < n; i++) {
            int nextCity = findNearestCity(graph, currentCity, visited);
            path[i] = nextCity;
            totalCost += graph[currentCity][nextCity];
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        // Return to the starting city to complete the tour
        path[n] = path[0];
        totalCost += graph[currentCity][path[0]];

        return new TspResult(path, totalCost);
    }

    /**
     * Finds the nearest unvisited city from the current city.
     *
     * @param graph       The distance matrix
     * @param currentCity The current city
     * @param visited     Array indicating which cities have been visited
     * @return The index of the nearest unvisited city
     */
    private int findNearestCity(int[][] graph, int currentCity, boolean[] visited) {
        int n = graph.length;
        int nearestCity = -1;
        int shortestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            // Skip if already visited or if it's the current city
            if (visited[i] || i == currentCity) {
                continue;
            }

            // Check if this city is closer than the current nearest
            if (graph[currentCity][i] < shortestDistance) {
                shortestDistance = graph[currentCity][i];
                nearestCity = i;
            }
        }

        // If no unvisited city was found, return -1 (shouldn't happen in a complete graph)
        return nearestCity;
    }
}
