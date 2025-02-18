package polsl.tsp.simple;

import java.util.Arrays;

/**
 * PROMPT: Create a TSP solution in Java
 */
public class SimpleChatGptSolution {
    private static final int INF = Integer.MAX_VALUE;
    private int n;
    private int[][] distanceMatrix;
    private boolean[] visited;
    private int minCost = INF;
    private int[] bestRoute;

    public SimpleChatGptSolution(int[][] distanceMatrix) {
        this.n = distanceMatrix.length;
        this.distanceMatrix = distanceMatrix;
        this.visited = new boolean[n];
        this.bestRoute = new int[n + 1];
    }

    public void solveTSP() {
        int[] currentRoute = new int[n + 1];
        visited[0] = true;
        tspUtil(0, 1, 0, currentRoute);
    }

    private void tspUtil(int currentPosition, int count, int cost, int[] currentRoute) {
        currentRoute[count - 1] = currentPosition;

        if (count == n && distanceMatrix[currentPosition][0] > 0) {
            int totalCost = cost + distanceMatrix[currentPosition][0];
            if (totalCost < minCost) {
                minCost = totalCost;
                System.arraycopy(currentRoute, 0, bestRoute, 0, n);
                bestRoute[n] = 0;
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i] && distanceMatrix[currentPosition][i] > 0) {
                visited[i] = true;
                tspUtil(i, count + 1, cost + distanceMatrix[currentPosition][i], currentRoute);
                visited[i] = false;
            }
        }
    }

    public void printSolution() {
        System.out.println("Minimum Cost: " + minCost);
        System.out.print("Best Route: ");
        for (int i : bestRoute) {
            System.out.print(i + " -> ");
        }
        System.out.println("0");
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
                { 0, 10, 15, 20 },
                { 10, 0, 35, 25 },
                { 15, 35, 0, 30 },
                { 20, 25, 30, 0 }
        };

        SimpleChatGptSolution tsp = new SimpleChatGptSolution(distanceMatrix);
        tsp.solveTSP();
        tsp.printSolution();
    }
}
