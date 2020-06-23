package dijkstra;

public class SquareGrid extends GraphAL {

    private int x_size;
    private int y_size;
    private int[][] grid;

    public SquareGrid(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
        grid = new int[x_size][y_size];
        int n = x_size*y_size;
    }

    public void setSquare(int x, int y, int val) {
        grid[x][y] = val;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getX_size() {
        return x_size;
    }

    public int getY_size() {
        return y_size;
    }
}

