package polsl.tsp.complex;

public class TspResult {
    private final int[] path;
    private final int cost;

    public TspResult(int[] path, int cost) {
        this.path = path;
        this.cost = cost;
    }

    public int[] getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }
}
