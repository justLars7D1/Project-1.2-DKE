package dijkstra;

public class GraphM implements Graph {

    private double[][] matrix;
    private Object[] nodeValues;
    private int numEdge;

    // Initialize the graph with n vertices
    @Override
    public void init(int n) {
        matrix = new double[n][n];
        nodeValues = new Object[n];
        numEdge = 0;
    }

    @Override
    public int nodeCount() {
        return nodeValues.length;
    }

    @Override
    public int edgeCount() {
        return numEdge;
    }

    @Override
    public Object getValue(int v) {
        return nodeValues[v];
    }

    @Override
    public void setValue(int v, Object val) {
        nodeValues[v] = val;
    }

    @Override
    public void addEdge(int v, int w, double wgt) {
        if (wgt == 0) return;
        matrix[v][w] = wgt;
        numEdge++;
    }

    @Override
    public double weight(int v, int w) {
        return matrix[v][w];
    }

    @Override
    public void removeEdge(int v, int w) {
        matrix[v][w] = 0;
        numEdge--;
    }

    @Override
    public int[] neighbors(int v) {
        int size = 0;
        int[] neighbors;
        for (int i = 0; i < nodeValues.length; i++) {
            if (matrix[v][i] != 0) size++;
        }
        neighbors = new int[size];
        size = 0;
        for (int i = 0; i < nodeValues.length; i++) {
            if (matrix[v][i] != 0) neighbors[size++] = i;
        }
        return neighbors;
    }
}
