package polsl.tsp.complex;

import java.util.*;

public class ComplexChatGptSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        int n = graph.length;
        List<Integer> cities = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            cities.add(i);
        }

        int minCost = Integer.MAX_VALUE;
        int[] bestPath = new int[n + 1]; // Including return to starting city

        for (List<Integer> perm : permute(cities)) {
            int[] path = new int[n + 1];
            path[0] = 0; // starting at city 0
            for (int i = 0; i < perm.size(); i++) {
                path[i + 1] = perm.get(i);
            }
            path[n] = 0; // return to start

            int cost = calculatePathCost(graph, path);
            if (cost < minCost) {
                minCost = cost;
                bestPath = path.clone();
            }
        }

        return new TspResult(bestPath, minCost);
    }

    private int calculatePathCost(int[][] graph, int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph[path[i]][path[i + 1]];
        }
        return cost;
    }

    private List<List<Integer>> permute(List<Integer> nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, new ArrayList<>(), new boolean[nums.size()], result);
        return result;
    }

    private void backtrack(List<Integer> nums, List<Integer> current, boolean[] used, List<List<Integer>> result) {
        if (current.size() == nums.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < nums.size(); i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(nums.get(i));
            backtrack(nums, current, used, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
}
