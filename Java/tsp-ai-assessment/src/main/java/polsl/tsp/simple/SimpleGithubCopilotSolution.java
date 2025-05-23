package polsl.tsp.simple;

import java.util.*;

public class SimpleGithubCopilotSolution {
    private final int n; // number of cities
    private final double[][] dist; // distance matrix
    private double bestCost = Double.POSITIVE_INFINITY;
    private List<Integer> bestTour;

    public SimpleGithubCopilotSolution(double[][] dist) {
        this.n = dist.length;
        this.dist = dist;
    }

    // Brute-force method for TSP (works for small n)
    public List<Integer> solve() {
        List<Integer> cities = new ArrayList<>();
        for (int i = 0; i < n; i++) cities.add(i);

        bestTour = new ArrayList<>();
        permute(cities, 1); // Fix starting city as 0
        return bestTour;
    }

    private void permute(List<Integer> cities, int start) {
        if (start == cities.size()) {
            double cost = tourCost(cities);
            if (cost < bestCost) {
                bestCost = cost;
                bestTour = new ArrayList<>(cities);
            }
            return;
        }
        for (int i = start; i < cities.size(); i++) {
            Collections.swap(cities, start, i);
            permute(cities, start + 1);
            Collections.swap(cities, start, i);
        }
    }

    private double tourCost(List<Integer> tour) {
        double cost = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            cost += dist[tour.get(i)][tour.get(i+1)];
        }
        cost += dist[tour.get(tour.size() - 1)][tour.get(0)]; // return to start
        return cost;
    }

    public double getBestCost() {
        return bestCost;
    }

    // Example usage
    public static void main(String[] args) {
        double[][] dist = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };
        SimpleGithubCopilotSolution solver = new SimpleGithubCopilotSolution(dist);
        List<Integer> tour = solver.solve();
        System.out.println("Best tour: " + tour);
        System.out.println("Cost: " + solver.getBestCost());
    }
}
