package polsl.tsp.complex;

public class ComplexDeepSeekNNSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        if (graph == null || graph.length == 0 || graph.length != graph[0].length) {
            throw new IllegalArgumentException("Invalid graph input");
        }

        int n = graph.length;
        if (n == 1) {
            return new TspResult(new int[]{0}, 0);
        }

        int[] path = new int[n + 1];
        boolean[] visited = new boolean[n];
        int totalCost = 0;

        // Start at city 0
        int currentCity = 0;
        path[0] = currentCity;
        visited[currentCity] = true;

        for (int i = 1; i < n; i++) {
            int nextCity = findNearestUnvisitedCity(currentCity, graph, visited);
            totalCost += graph[currentCity][nextCity];
            path[i] = nextCity;
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        // Return to starting city
        totalCost += graph[currentCity][0];
        path[n] = 0;

        return new TspResult(path, totalCost);
    }

    private int findNearestUnvisitedCity(int currentCity, int[][] graph, boolean[] visited) {
        int minDistance = Integer.MAX_VALUE;
        int nearestCity = -1;

        for (int i = 0; i < graph.length; i++) {
            if (!visited[i] && graph[currentCity][i] < minDistance) {
                minDistance = graph[currentCity][i];
                nearestCity = i;
            }
        }

        return nearestCity;
    }
}
