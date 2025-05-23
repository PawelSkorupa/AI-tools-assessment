package polsl.tsp.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides a solution to the Traveling Salesperson Problem (TSP)
 * using the Nearest Neighbor heuristic.
 *
 * The Nearest Neighbor heuristic is a greedy algorithm that starts at an
 * arbitrary city and repeatedly visits the nearest unvisited city until
 * all cities have been visited. Finally, it returns to the starting city.
 * While simple and efficient, it does not guarantee an optimal solution.
 */
public class SimpleGeminiSolution {

    private double[][] distanceMatrix; // Stores the distances between cities
    private int numberOfCities;        // Total number of cities

    /**
     * Constructor for the TSPSolver.
     *
     * @param distanceMatrix A 2D array representing the distance between any two cities.
     * distanceMatrix[i][j] should be the distance from city i to city j.
     * It's assumed to be symmetric (distanceMatrix[i][j] == distanceMatrix[j][i]).
     * Diagonal elements (distanceMatrix[i][i]) should be 0.
     */
    public SimpleGeminiSolution(double[][] distanceMatrix) {
        if (distanceMatrix == null || distanceMatrix.length == 0) {
            throw new IllegalArgumentException("Distance matrix cannot be null or empty.");
        }
        this.distanceMatrix = distanceMatrix;
        this.numberOfCities = distanceMatrix.length;

        // Basic validation for square matrix
        for (int i = 0; i < numberOfCities; i++) {
            if (distanceMatrix[i].length != numberOfCities) {
                throw new IllegalArgumentException("Distance matrix must be square.");
            }
        }
    }

    /**
     * Solves the TSP using the Nearest Neighbor heuristic starting from a specified city.
     *
     * @param startCity The index of the city to start the tour from.
     * @return A list of integers representing the tour path (sequence of city indices).
     * The list includes the start city at the beginning and end.
     */
    public List<Integer> solveNearestNeighbor(int startCity) {
        if (startCity < 0 || startCity >= numberOfCities) {
            throw new IllegalArgumentException("Start city index is out of bounds.");
        }

        List<Integer> tour = new ArrayList<>(); // Stores the sequence of cities visited
        boolean[] visited = new boolean[numberOfCities]; // Tracks visited cities

        // Add the starting city to the tour and mark it as visited
        tour.add(startCity);
        visited[startCity] = true;

        int currentCity = startCity;
        double totalDistance = 0; // To keep track of the total tour distance

        // Continue until all cities have been visited
        while (tour.size() < numberOfCities) {
            int nextCity = -1;
            double minDistance = Double.MAX_VALUE;

            // Find the nearest unvisited city from the current city
            for (int i = 0; i < numberOfCities; i++) {
                if (!visited[i]) { // If city 'i' has not been visited
                    if (distanceMatrix[currentCity][i] < minDistance) {
                        minDistance = distanceMatrix[currentCity][i];
                        nextCity = i;
                    }
                }
            }

            // If a next city is found, add it to the tour and update current city
            if (nextCity != -1) {
                tour.add(nextCity);
                visited[nextCity] = true;
                totalDistance += minDistance; // Add the distance to the total
                currentCity = nextCity;
            } else {
                // This case should ideally not be reached if the graph is connected
                // and all cities are reachable.
                System.err.println("Error: No unvisited city found. Something went wrong.");
                break;
            }
        }

        // Return to the starting city to complete the tour
        tour.add(startCity);
        totalDistance += distanceMatrix[currentCity][startCity]; // Add distance back to start

        System.out.println("Nearest Neighbor Tour Distance: " + totalDistance);
        return tour;
    }

    /**
     * Main method for demonstration.
     */
    public static void main(String[] args) {
        // Example Usage:
        // Define a distance matrix for 4 cities (A, B, C, D)
        // Indices: 0=A, 1=B, 2=C, 3=D
        double[][] distances = {
                // A   B   C   D
                {0.0, 10.0, 15.0, 20.0}, // A to A, B, C, D
                {10.0, 0.0, 35.0, 25.0}, // B to A, B, C, D
                {15.0, 35.0, 0.0, 30.0}, // C to A, B, C, D
                {20.0, 25.0, 30.0, 0.0}  // D to A, B, C, D
        };

        SimpleGeminiSolution solver = new SimpleGeminiSolution(distances);

        System.out.println("Solving TSP using Nearest Neighbor heuristic:");

        // Try starting from city 0 (A)
        int startCity = 0;
        List<Integer> tour = solver.solveNearestNeighbor(startCity);
        System.out.println("Tour starting from city " + startCity + ": " + tour);
        System.out.println("--------------------------------------------------");

        // Try starting from city 1 (B)
        startCity = 1;
        tour = solver.solveNearestNeighbor(startCity);
        System.out.println("Tour starting from city " + startCity + ": " + tour);
        System.out.println("--------------------------------------------------");

        // Example with more cities
        double[][] largerDistances = {
                {0, 12, 10, 19, 8},
                {12, 0, 3, 7, 2},
                {10, 3, 0, 6, 20},
                {19, 7, 6, 0, 4},
                {8, 2, 20, 4, 0}
        };
        SimpleGeminiSolution largerSolver = new SimpleGeminiSolution(largerDistances);
        System.out.println("Solving TSP for 5 cities:");
        List<Integer> largerTour = largerSolver.solveNearestNeighbor(0);
        System.out.println("Tour starting from city 0: " + largerTour);
    }
}
