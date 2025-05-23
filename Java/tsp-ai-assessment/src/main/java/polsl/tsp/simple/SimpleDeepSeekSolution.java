package polsl.tsp.simple;

import java.util.*;

public class SimpleDeepSeekSolution {
    private int[][] distanceMatrix;
    private int numberOfCities;
    private boolean[] visited;
    private int[] finalPath;
    private int minDistance = Integer.MAX_VALUE;

    public SimpleDeepSeekSolution(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numberOfCities = distanceMatrix.length;
        this.visited = new boolean[numberOfCities];
        this.finalPath = new int[numberOfCities + 1];
    }

    // Brute-force solution for small instances (exact solution)
    public void solveBruteForce() {
        if (numberOfCities > 10) {
            System.out.println("Too many cities for brute-force approach. Using heuristic instead.");
            solveWithNearestNeighbor();
            return;
        }

        int[] currentPath = new int[numberOfCities + 1];
        currentPath[0] = 0; // Start from city 0
        visited[0] = true;
        bruteForceRecursion(currentPath, 1, 0);

        System.out.println("Optimal path found with brute-force:");
        printSolution();
    }

    private void bruteForceRecursion(int[] currentPath, int level, int currentDistance) {
        if (level == numberOfCities) {
            // Complete the cycle back to the starting city
            int totalDistance = currentDistance + distanceMatrix[currentPath[level - 1]][0];
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                System.arraycopy(currentPath, 0, finalPath, 0, numberOfCities);
                finalPath[numberOfCities] = 0; // Return to start
            }
            return;
        }

        for (int i = 0; i < numberOfCities; i++) {
            if (!visited[i]) {
                int newDistance = currentDistance + distanceMatrix[currentPath[level - 1]][i];
                if (newDistance < minDistance) { // Prune if already worse than best
                    currentPath[level] = i;
                    visited[i] = true;
                    bruteForceRecursion(currentPath, level + 1, newDistance);
                    visited[i] = false;
                }
            }
        }
    }

    // Heuristic solution for larger instances (approximate solution)
    public void solveWithNearestNeighbor() {
        Arrays.fill(visited, false);
        int[] path = new int[numberOfCities + 1];
        path[0] = 0; // Start from city 0
        visited[0] = true;
        minDistance = 0;

        for (int i = 1; i < numberOfCities; i++) {
            int last = path[i - 1];
            int nextCity = -1;
            int minDist = Integer.MAX_VALUE;

            // Find nearest unvisited neighbor
            for (int j = 0; j < numberOfCities; j++) {
                if (!visited[j] && distanceMatrix[last][j] < minDist) {
                    minDist = distanceMatrix[last][j];
                    nextCity = j;
                }
            }

            path[i] = nextCity;
            visited[nextCity] = true;
            minDistance += minDist;
        }

        // Return to starting city
        minDistance += distanceMatrix[path[numberOfCities - 1]][0];
        path[numberOfCities] = 0;
        finalPath = path;

        System.out.println("Approximate path found with Nearest Neighbor heuristic:");
        printSolution();
    }

    private void printSolution() {
        System.out.println("Minimum distance: " + minDistance);
        System.out.print("Path: ");
        for (int i = 0; i <= numberOfCities; i++) {
            System.out.print(finalPath[i]);
            if (i < numberOfCities) System.out.print(" -> ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Example distance matrix (symmetric TSP)
        int[][] distanceMatrix = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        SimpleDeepSeekSolution simpleDeepSeekSolution = new SimpleDeepSeekSolution(distanceMatrix);

        System.out.println("Solving TSP for " + distanceMatrix.length + " cities...");
        simpleDeepSeekSolution.solveBruteForce();
    }
}
