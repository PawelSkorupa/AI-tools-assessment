package polsl.tsp.complex;

import java.util.ArrayList;
import java.util.List;

public class ComplexDeepSeekBruteForceSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        if (graph == null || graph.length == 0 || graph.length != graph[0].length) {
            throw new IllegalArgumentException("Invalid graph input");
        }

        int n = graph.length;
        if (n == 1) {
            return new TspResult(new int[]{0}, 0);
        }

        List<Integer> cities = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            cities.add(i);
        }

        int[] minPath = new int[n + 1];
        int minCost = Integer.MAX_VALUE;

        // Generate all permutations and find the one with minimum cost
        List<List<Integer>> permutations = generatePermutations(cities);
        for (List<Integer> permutation : permutations) {
            int currentCost = 0;
            int from = 0; // start at city 0

            // Calculate cost for this permutation
            for (int to : permutation) {
                currentCost += graph[from][to];
                from = to;
            }
            // Return to starting city
            currentCost += graph[from][0];

            // Update minimum if found
            if (currentCost < minCost) {
                minCost = currentCost;
                // Build the path (0 -> permutation -> 0)
                minPath[0] = 0;
                for (int i = 0; i < permutation.size(); i++) {
                    minPath[i + 1] = permutation.get(i);
                }
                minPath[n] = 0;
            }
        }

        return new TspResult(minPath, minCost);
    }

    private List<List<Integer>> generatePermutations(List<Integer> cities) {
        List<List<Integer>> result = new ArrayList<>();
        permute(cities, 0, result);
        return result;
    }

    private void permute(List<Integer> arr, int k, List<List<Integer>> result) {
        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1, result);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            result.add(new ArrayList<>(arr));
        }
    }
}
