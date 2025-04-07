package polsl.tsp.complex;
import java.util.Arrays;

public class ComplexMsCopilotSolution implements TspAlgorithm {

    @Override
    public TspResult solveTSP(int[][] graph) {
        int n = graph.length;
        int[] vertices = new int[n - 1];
        for (int i = 1; i < n; i++) {
            vertices[i - 1] = i;
        }

        int[] bestPath = null;
        int minCost = Integer.MAX_VALUE;

        // Generate all permutations of vertices
        do {
            int currentCost = 0;
            int currentVertex = 0;

            // Calculate the cost of the current path
            for (int nextVertex : vertices) {
                currentCost += graph[currentVertex][nextVertex];
                currentVertex = nextVertex;
            }
            currentCost += graph[currentVertex][0]; // Return to the starting point

            if (currentCost < minCost) {
                minCost = currentCost;
                bestPath = Arrays.copyOf(vertices, vertices.length + 1);
                bestPath[0] = 0;
                bestPath[bestPath.length - 1] = 0;
            }
        } while (nextPermutation(vertices));

        return new TspResult(bestPath, minCost);
    }

    private boolean nextPermutation(int[] array) {
        int i = array.length - 2;
        while (i >= 0 && array[i] >= array[i + 1]) {
            i--;
        }
        if (i < 0) return false;

        int j = array.length - 1;
        while (array[j] <= array[i]) {
            j--;
        }
        swap(array, i, j);

        reverse(array, i + 1);
        return true;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void reverse(int[] array, int start) {
        int end = array.length - 1;
        while (start < end) {
            swap(array, start, end);
            start++;
            end--;
        }
    }
}
