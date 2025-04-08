package polsl.tsp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import static org.junit.jupiter.api.Assertions.*;

import polsl.tsp.complex.TspAlgorithm;
import polsl.tsp.complex.TspResult;

public class TspTest {

    @ParameterizedTest(name = "[{0}] testTSPSolution")
    @ArgumentsSource(TspSolverProvider.class)
    public void testTSPSolution4x4(String solverName, TspAlgorithm solver) {
        GraphUtils.SampleTspData data = GraphUtils.getSampleGraph4x4();

        int[][] graph = data.getGraph();
        TspResult expected = data.getExpectedResult();

        TspResult actual = solver.solveTSP(graph);

        assertTrue(GraphUtils.validatePath(actual.getPath(), graph.length), solverName + ": Invalid path!");
        assertEquals(expected.getCost(), actual.getCost(), solverName + ": Incorrect cost!");
        assertArrayEquals(expected.getPath(), actual.getPath(), "Actual path does not match the expected shortest path!");
    }

    @ParameterizedTest(name = "[{0}] testTSPSolution")
    @ArgumentsSource(TspSolverProvider.class)
    public void testTSPSolution10x10(String solverName, TspAlgorithm solver) {
        GraphUtils.SampleTspData data = GraphUtils.getSampleGraph10x10();
        int[][] graph = data.getGraph();
        TspResult expected = data.getExpectedResult();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime();
        TspResult actual = solver.solveTSP(graph);
        long endTime = System.nanoTime();

        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long timeMillis = (endTime - startTime) / 1_000_000;
        long memKB = (afterUsedMem - beforeUsedMem) / 1024;

        System.out.println(solverName + ": Time = " + timeMillis + " ms, Memory = " + memKB + " KB");

        assertTrue(GraphUtils.validatePath(actual.getPath(), graph.length), solverName + ": Invalid path!");
        assertEquals(expected.getCost(), actual.getCost(), solverName + ": Incorrect cost!");
        assertArrayEquals(expected.getPath(), actual.getPath(), solverName + ": Incorrect path!");
    }

    @ParameterizedTest(name = "[{0}] testTSPSolution")
    @ArgumentsSource(TspSolverProvider.class)
    @Disabled
    public void testTSPSolution15x15(String solverName, TspAlgorithm solver) {
        GraphUtils.SampleTspData data = GraphUtils.getSampleGraph15x15();

        int[][] graph = data.getGraph();
        TspResult expected = data.getExpectedResult();

        TspResult actual = solver.solveTSP(graph);

        assertTrue(GraphUtils.validatePath(actual.getPath(), graph.length), solverName + ": Invalid path!");
        assertEquals(expected.getCost(), actual.getCost(), solverName + ": Incorrect cost!");
    }

    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testAsymmetricalGraphWithExpectedResult(String solverName, TspAlgorithm solver) {
        GraphUtils.SampleTspData data = GraphUtils.getSampleAsymmetricalGraph();
        int[][] graph = data.getGraph();
        TspResult expected = data.getExpectedResult();

        TspResult actual = solver.solveTSP(graph);

        assertTrue(GraphUtils.validatePath(actual.getPath(), graph.length), solverName + ": Path is invalid!");
        assertEquals(expected.getCost(), actual.getCost(), solverName + ": Cost does not match expected!");
        assertArrayEquals(expected.getPath(), actual.getPath(), solverName + ": Path does not match expected!");
    }


    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testNullInput(String solverName, TspAlgorithm solver) {
        assertThrows(IllegalArgumentException.class, () -> solver.solveTSP(null), solverName + ": Solver should throw on null input");
    }

    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testEmptyMatrix(String solverName, TspAlgorithm solver) {
        int[][] emptyGraph = new int[0][0];
        assertThrows(IllegalArgumentException.class, () -> solver.solveTSP(emptyGraph), solverName + ": Solver should throw on empty matrix");
    }

    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testNonSquareMatrix(String solverName, TspAlgorithm solver) {
        int[][] nonSquareGraph = {
                {0, 10, 15},
                {10, 0, 20}
                // Missing third row
        };
        assertThrows(IllegalArgumentException.class, () -> solver.solveTSP(nonSquareGraph), solverName + ": Solver should throw on non-square matrix");
    }

    @ParameterizedTest(name = "[{0}] testInvalidGraph")
    @ArgumentsSource(TspSolverProvider.class)
    public void testMatrixWithNullRows(String solverName, TspAlgorithm solver) {
        int[][] badGraph = new int[3][];
        badGraph[0] = new int[]{0, 1, 2};
        badGraph[1] = null;
        badGraph[2] = new int[]{2, 1, 0};

        assertThrows(IllegalArgumentException.class, () -> solver.solveTSP(badGraph), solverName + ": Solver should throw on matrix with null rows");
    }

}
