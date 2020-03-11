package project12.physicsengine;

public class Vector2d extends Vector {

    public Vector2d(double x, double y) {
        super(x, y);
    }

    public Vector2d() {
        super(0, 0);
    }

    public double get_x() {
        return coords[0];
    }

    public double get_y() {
        return coords[1];
    }

}
