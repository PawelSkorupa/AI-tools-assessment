package polsl.tsp.simple;

import java.util.*;

public class SimpleTabnineSolution {
    private int[][] distanceMatrix;
    private int numCities;

    public SimpleTabnineSolution(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numCities = distanceMatrix.length;
    }

    public List<Integer> solve() {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[numCities];
        
        // Start from the first city
        int currentCity = 0;
        tour.add(currentCity);
        visited[currentCity] = true;

        for (int i = 1; i < numCities; i++) {
            int nextCity = findNearestUnvisitedCity(currentCity, visited);
            tour.add(nextCity);
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        // Return to the starting city
        tour.add(0);

        return tour;
    }

    private int findNearestUnvisitedCity(int currentCity, boolean[] visited) {
        int nearestCity = -1;
        int shortestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < numCities; i++) {
            if (!visited[i] && distanceMatrix[currentCity][i] < shortestDistance) {
                nearestCity = i;
                shortestDistance = distanceMatrix[currentCity][i];
            }
        }

        return nearestCity;
    }

    public int calculateTourLength(List<Integer> tour) {
        int length = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            length += distanceMatrix[tour.get(i)][tour.get(i + 1)];
        }
        return length;
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        SimpleTabnineSolution tsp = new SimpleTabnineSolution(distanceMatrix);
        List<Integer> tour = tsp.solve();
        int tourLength = tsp.calculateTourLength(tour);

        System.out.println("Tour: " + tour);
        System.out.println("Tour length: " + tourLength);
    }
}