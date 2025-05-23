package polsl.tsp.complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplexGeminiSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        if (graph == null || graph.length == 0 || graph[0].length == 0) {
            throw new IllegalArgumentException("Graph cannot be null or empty.");
        }

        int numNodes = graph.length;
        if (numNodes == 1) {
            return new TspResult(new int[]{0}, 0); // Single node, path is just the node itself, cost 0
        }

        // Generate all possible permutations of nodes (excluding the starting node)
        List<Integer> nodesToPermute = new ArrayList<>();
        for (int i = 1; i < numNodes; i++) {
            nodesToPermute.add(i);
        }

        long minCost = Long.MAX_VALUE;
        int[] bestPath = null;

        // Using a custom permutation generator for simplicity, could also use a library
        List<List<Integer>> permutations = generatePermutations(nodesToPermute);

        for (List<Integer> permutation : permutations) {
            long currentCost = 0;
            int[] currentPath = new int[numNodes + 1]; // +1 for returning to the start
            currentPath[0] = 0; // Start at node 0

            // Construct the full path with the starting node and the current permutation
            for (int i = 0; i < permutation.size(); i++) {
                currentPath[i + 1] = permutation.get(i);
            }
            currentPath[numNodes] = 0; // Return to node 0

            boolean pathValid = true;
            for (int i = 0; i < numNodes; i++) {
                int from = currentPath[i];
                int to = currentPath[i + 1];

                // Check for valid edge (e.g., non-negative cost)
                if (graph[from][to] < 0) { // Assuming negative values mean no direct edge
                    pathValid = false;
                    break;
                }
                currentCost += graph[from][to];
            }

            if (pathValid && currentCost < minCost) {
                minCost = currentCost;
                // Copy the best path
                bestPath = new int[numNodes + 1];
                System.arraycopy(currentPath, 0, bestPath, 0, numNodes + 1);
            }
        }

        if (bestPath == null) {
            // This case might happen if no valid path exists (e.g., disconnected graph)
            return new TspResult(new int[]{}, -1); // Or throw an exception
        }

        return new TspResult(bestPath, (int) minCost);
    }

    /**
     * Helper method to generate all permutations of a list of integers.
     * This is a basic recursive implementation.
     */
    private List<List<Integer>> generatePermutations(List<Integer> original) {
        List<List<Integer>> permutations = new ArrayList<>();
        if (original.isEmpty()) {
            permutations.add(new ArrayList<>());
            return permutations;
        }

        Integer firstElement = original.remove(0);
        List<List<Integer>> subPermutations = generatePermutations(original);

        for (List<Integer> subPermutation : subPermutations) {
            for (int i = 0; i <= subPermutation.size(); i++) {
                List<Integer> newPermutation = new ArrayList<>(subPermutation);
                newPermutation.add(i, firstElement);
                permutations.add(newPermutation);
            }
        }
        return permutations;
    }
}
