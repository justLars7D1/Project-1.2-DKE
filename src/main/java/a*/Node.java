public class Node {
    // Parent node (node that allowed "to arrive" to this node)
    private Node p;

    // Distance from the starting point to this node.
    private double g;
    // Flight distance between this node and the end node.
    private double h;
    // Sum of G and H.
    private double f;

    // Whether this node is a wall.
    private boolean isWall;

    private final int x;
    private final int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getPosX() {
        return this.x;
    }

    public int getPosY() {
        return this.y;
    }

    // @return Distance from the starting point to this node.

    public double getG() {
        return this.g;
    }

    //@return Flight distance between this node and the ending node.

    public double getH() {
        return this.h;
    }

    //@return Sum of G and H.

    public double getF() {
        return this.f;
    }

    //@return Parent node (note that allowed "to arrive" to this node).

    public Node getP() {
        return this.p;
    }

    // @return Whether this node is a wall.

    public boolean isWall() {
        return this.isWall;
    }

    //@param g Distance from the starting point to this node.

    public void setG(double g) {
        this.g = g;
    }

    //@param h Flight distance between this node and the end node.

    public void setH(double h) {
        this.h = h;
    }

    //@param f Sum of G and H.
    public void setF(double f) {
        this.f = f;
    }

    //@param p Parent node (node that allowed "to arrive" to this node).
    public void setP(Node p) {
        this.p = p;
    }

    //@param Whether this node is a wall.
    public void isWall(boolean isWall) {
        this.isWall = isWall;
    }
}
