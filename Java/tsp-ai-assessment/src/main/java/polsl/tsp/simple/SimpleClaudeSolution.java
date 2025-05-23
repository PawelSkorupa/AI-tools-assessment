package polsl.tsp.simple;

import java.util.*;

public class SimpleClaudeSolution {
    private double[][] distances;
    private int numCities;
    private Random random;

    public SimpleClaudeSolution(double[][] distances) {
        this.distances = distances;
        this.numCities = distances.length;
        this.random = new Random(42); // Fixed seed for reproducible results
    }

    // City class to represent coordinates
    public static class City {
        public double x, y;
        public String name;

        public City(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public double distanceTo(City other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }

        @Override
        public String toString() {
            return name + "(" + x + "," + y + ")";
        }
    }

    // Result class to store solution details
    public static class TSPResult {
        public int[] tour;
        public double distance;
        public long timeMs;
        public String algorithm;

        public TSPResult(int[] tour, double distance, long timeMs, String algorithm) {
            this.tour = tour.clone();
            this.distance = distance;
            this.timeMs = timeMs;
            this.algorithm = algorithm;
        }
    }

    // Brute force solution (only practical for small instances)
    public TSPResult solveBruteForce() {
        long startTime = System.currentTimeMillis();

        if (numCities > 10) {
            return new TSPResult(new int[0], Double.MAX_VALUE, 0, "Brute Force (skipped - too large)");
        }

        List<Integer> cities = new ArrayList<>();
        for (int i = 1; i < numCities; i++) {
            cities.add(i);
        }

        int[] bestTour = null;
        double bestDistance = Double.MAX_VALUE;

        // Generate all permutations
        List<List<Integer>> permutations = generatePermutations(cities);

        for (List<Integer> perm : permutations) {
            int[] tour = new int[numCities + 1];
            tour[0] = 0; // Start from city 0
            for (int i = 0; i < perm.size(); i++) {
                tour[i + 1] = perm.get(i);
            }
            tour[numCities] = 0; // Return to start

            double distance = calculateTourDistance(tour);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestTour = tour.clone();
            }
        }

        long endTime = System.currentTimeMillis();
        return new TSPResult(bestTour, bestDistance, endTime - startTime, "Brute Force");
    }

    // Nearest Neighbor heuristic
    public TSPResult solveNearestNeighbor() {
        long startTime = System.currentTimeMillis();

        int[] tour = new int[numCities + 1];
        boolean[] visited = new boolean[numCities];

        // Start from city 0
        tour[0] = 0;
        visited[0] = true;

        for (int i = 1; i < numCities; i++) {
            int currentCity = tour[i - 1];
            int nearestCity = -1;
            double nearestDistance = Double.MAX_VALUE;

            // Find nearest unvisited city
            for (int j = 0; j < numCities; j++) {
                if (!visited[j] && distances[currentCity][j] < nearestDistance) {
                    nearestDistance = distances[currentCity][j];
                    nearestCity = j;
                }
            }

            tour[i] = nearestCity;
            visited[nearestCity] = true;
        }

        tour[numCities] = 0; // Return to start

        long endTime = System.currentTimeMillis();
        double distance = calculateTourDistance(tour);
        return new TSPResult(tour, distance, endTime - startTime, "Nearest Neighbor");
    }

    // 2-opt improvement heuristic
    public TSPResult solve2Opt() {
        long startTime = System.currentTimeMillis();

        // Start with nearest neighbor solution
        TSPResult initial = solveNearestNeighbor();
        int[] tour = initial.tour.clone();

        boolean improved = true;
        while (improved) {
            improved = false;

            for (int i = 1; i < numCities - 1; i++) {
                for (int j = i + 1; j < numCities; j++) {
                    // Try 2-opt swap
                    int[] newTour = perform2OptSwap(tour, i, j);
                    double newDistance = calculateTourDistance(newTour);

                    if (newDistance < calculateTourDistance(tour)) {
                        tour = newTour;
                        improved = true;
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        double distance = calculateTourDistance(tour);
        return new TSPResult(tour, distance, endTime - startTime, "2-Opt");
    }

    // Genetic Algorithm solution
    public TSPResult solveGeneticAlgorithm() {
        long startTime = System.currentTimeMillis();

        int populationSize = Math.min(100, numCities * 10);
        int generations = 1000;
        double mutationRate = 0.02;
        double eliteRate = 0.1;

        // Initialize population
        List<int[]> population = initializePopulation(populationSize);

        for (int gen = 0; gen < generations; gen++) {
            // Calculate fitness for all individuals
            List<Double> fitness = new ArrayList<>();
            for (int[] individual : population) {
                double distance = calculateTourDistance(individual);
                fitness.add(1.0 / (1.0 + distance)); // Higher fitness for shorter distance
            }

            // Create new population
            List<int[]> newPopulation = new ArrayList<>();

            // Keep elite individuals
            int eliteCount = (int) (populationSize * eliteRate);
            List<Integer> sortedIndices = getSortedIndices(fitness);
            for (int i = 0; i < eliteCount; i++) {
                newPopulation.add(population.get(sortedIndices.get(sortedIndices.size() - 1 - i)).clone());
            }

            // Generate offspring
            while (newPopulation.size() < populationSize) {
                int[] parent1 = tournamentSelection(population, fitness);
                int[] parent2 = tournamentSelection(population, fitness);
                int[] offspring = crossover(parent1, parent2);

                if (random.nextDouble() < mutationRate) {
                    mutate(offspring);
                }

                newPopulation.add(offspring);
            }

            population = newPopulation;
        }

        // Find best solution
        int[] bestTour = population.get(0);
        double bestDistance = calculateTourDistance(bestTour);

        for (int[] tour : population) {
            double distance = calculateTourDistance(tour);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestTour = tour;
            }
        }

        long endTime = System.currentTimeMillis();
        return new TSPResult(bestTour, bestDistance, endTime - startTime, "Genetic Algorithm");
    }

    // Helper methods
    private double calculateTourDistance(int[] tour) {
        double totalDistance = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            totalDistance += distances[tour[i]][tour[i + 1]];
        }
        return totalDistance;
    }

    private List<List<Integer>> generatePermutations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        if (list.size() == 0) {
            result.add(new ArrayList<>());
            return result;
        }

        for (int i = 0; i < list.size(); i++) {
            Integer element = list.get(i);
            List<Integer> remaining = new ArrayList<>(list);
            remaining.remove(i);

            for (List<Integer> perm : generatePermutations(remaining)) {
                perm.add(0, element);
                result.add(perm);
            }
        }
        return result;
    }

    private int[] perform2OptSwap(int[] tour, int i, int j) {
        int[] newTour = tour.clone();

        // Reverse the order of cities between i and j
        while (i < j) {
            int temp = newTour[i];
            newTour[i] = newTour[j];
            newTour[j] = temp;
            i++;
            j--;
        }

        return newTour;
    }

    private List<int[]> initializePopulation(int size) {
        List<int[]> population = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int[] individual = new int[numCities + 1];
            List<Integer> cities = new ArrayList<>();
            for (int j = 1; j < numCities; j++) {
                cities.add(j);
            }
            Collections.shuffle(cities, random);

            individual[0] = 0;
            for (int j = 0; j < cities.size(); j++) {
                individual[j + 1] = cities.get(j);
            }
            individual[numCities] = 0;

            population.add(individual);
        }

        return population;
    }

    private int[] tournamentSelection(List<int[]> population, List<Double> fitness) {
        int tournamentSize = 3;
        int bestIndex = random.nextInt(population.size());
        double bestFitness = fitness.get(bestIndex);

        for (int i = 1; i < tournamentSize; i++) {
            int index = random.nextInt(population.size());
            if (fitness.get(index) > bestFitness) {
                bestIndex = index;
                bestFitness = fitness.get(index);
            }
        }

        return population.get(bestIndex);
    }

    private int[] crossover(int[] parent1, int[] parent2) {
        // Order crossover (OX)
        int start = random.nextInt(numCities - 1) + 1;
        int end = random.nextInt(numCities - start) + start + 1;

        int[] offspring = new int[numCities + 1];
        offspring[0] = 0;
        offspring[numCities] = 0;

        // Copy segment from parent1
        Set<Integer> used = new HashSet<>();
        for (int i = start; i < end; i++) {
            offspring[i] = parent1[i];
            used.add(parent1[i]);
        }

        // Fill remaining positions with cities from parent2
        int pos = 1;
        for (int i = 1; i < numCities; i++) {
            if (!used.contains(parent2[i])) {
                while (pos >= start && pos < end) {
                    pos++;
                }
                if (pos < numCities) {
                    offspring[pos] = parent2[i];
                    pos++;
                }
            }
        }

        return offspring;
    }

    private void mutate(int[] individual) {
        // Swap mutation
        int pos1 = random.nextInt(numCities - 1) + 1;
        int pos2 = random.nextInt(numCities - 1) + 1;

        int temp = individual[pos1];
        individual[pos1] = individual[pos2];
        individual[pos2] = temp;
    }

    private List<Integer> getSortedIndices(List<Double> values) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            indices.add(i);
        }

        indices.sort((i, j) -> Double.compare(values.get(i), values.get(j)));
        return indices;
    }

    // Utility method to create distance matrix from cities
    public static double[][] createDistanceMatrix(City[] cities) {
        int n = cities.length;
        double[][] distances = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[i][j] = cities[i].distanceTo(cities[j]);
            }
        }

        return distances;
    }

    // Print tour with city names
    public static void printTour(int[] tour, City[] cities, TSPResult result) {
        System.out.println("\n" + result.algorithm + " Solution:");
        System.out.println("Distance: " + String.format("%.2f", result.distance));
        System.out.println("Time: " + result.timeMs + " ms");
        System.out.print("Tour: ");

        for (int i = 0; i < tour.length; i++) {
            if (cities != null && tour[i] < cities.length) {
                System.out.print(cities[tour[i]].name);
            } else {
                System.out.print("City" + tour[i]);
            }
            if (i < tour.length - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    // Main method with example usage
    public static void main(String[] args) {
        // Example 1: Small instance with coordinates
        System.out.println("=== TSP Solver Demo ===\n");

        // Create cities with coordinates
        City[] cities = {
                new City("A", 0, 0),
                new City("B", 1, 5),
                new City("C", 5, 3),
                new City("D", 6, 1),
                new City("E", 3, 0),
                new City("F", 2, 4)
        };

        System.out.println("Cities:");
        for (City city : cities) {
            System.out.println("  " + city);
        }

        // Create distance matrix
        double[][] distances = createDistanceMatrix(cities);
        SimpleClaudeSolution solver = new SimpleClaudeSolution(distances);

        // Solve with different algorithms
        System.out.println("\n=== Solutions ===");

        TSPResult bruteForce = solver.solveBruteForce();
        if (bruteForce.tour.length > 0) {
            printTour(bruteForce.tour, cities, bruteForce);
        } else {
            System.out.println("\nBrute Force: Skipped (too many cities)");
        }

        TSPResult nearestNeighbor = solver.solveNearestNeighbor();
        printTour(nearestNeighbor.tour, cities, nearestNeighbor);

        TSPResult twoOpt = solver.solve2Opt();
        printTour(twoOpt.tour, cities, twoOpt);

        TSPResult genetic = solver.solveGeneticAlgorithm();
        printTour(genetic.tour, cities, genetic);

        // Example 2: Larger random instance
        System.out.println("\n\n=== Larger Random Instance (15 cities) ===");

        Random rand = new Random(123);
        City[] largeCities = new City[15];
        for (int i = 0; i < 15; i++) {
            largeCities[i] = new City("C" + i, rand.nextDouble() * 100, rand.nextDouble() * 100);
        }

        double[][] largeDistances = createDistanceMatrix(largeCities);
        SimpleClaudeSolution largeSolver = new SimpleClaudeSolution(largeDistances);

        TSPResult largeNN = largeSolver.solveNearestNeighbor();
        TSPResult large2Opt = largeSolver.solve2Opt();
        TSPResult largeGA = largeSolver.solveGeneticAlgorithm();

        System.out.println("Nearest Neighbor: " + String.format("%.2f", largeNN.distance) + " (Time: " + largeNN.timeMs + " ms)");
        System.out.println("2-Opt: " + String.format("%.2f", large2Opt.distance) + " (Time: " + large2Opt.timeMs + " ms)");
        System.out.println("Genetic Algorithm: " + String.format("%.2f", largeGA.distance) + " (Time: " + largeGA.timeMs + " ms)");

        // Compare improvements
        double nnTo2Opt = ((largeNN.distance - large2Opt.distance) / largeNN.distance) * 100;
        double nnToGA = ((largeNN.distance - largeGA.distance) / largeNN.distance) * 100;

        System.out.println("\nImprovements over Nearest Neighbor:");
        System.out.println("2-Opt: " + String.format("%.1f", nnTo2Opt) + "%");
        System.out.println("Genetic Algorithm: " + String.format("%.1f", nnToGA) + "%");
    }
}
