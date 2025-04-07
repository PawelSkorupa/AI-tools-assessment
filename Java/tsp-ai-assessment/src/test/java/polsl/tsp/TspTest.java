package polsl.tsp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import static org.junit.jupiter.api.Assertions.*;

import polsl.tsp.complex.TspAlgorithm;
import polsl.tsp.complex.TspResult;

public class TspTest {

    @ParameterizedTest(name = "[{0}] testTSPSolution")
    @ArgumentsSource(TspSolverProvider.class)
    public void testTSPSolution(String solverName, TspAlgorithm solver) {
        int[][] graph = GraphUtils.generateSampleGraph();
        TspResult result = solver.solveTSP(graph);

        int[] path = result.getPath();
        int cost = result.getCost();

        int expectedCost = GraphUtils.calculateExpectedCost();
        assertEquals(expectedCost, cost, solverName + ": Solution cost is incorrect!");
        assertTrue(GraphUtils.validatePath(path, graph.length), solverName + ": Path is invalid!");
    }

    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testInvalidGraph(String solverName, TspAlgorithm solver) {
        int[][] invalidGraph = {
                {0, 10, 15},
                {15, 0, 25},
                {15, 25, 0}
        };

        TspResult result = solver.solveTSP(invalidGraph);
        int[] path = result.getPath();

        assertFalse(GraphUtils.validatePath(path, invalidGraph.length), solverName + ": Invalid graph not handled correctly!");
    }
}
