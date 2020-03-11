package project12.physicsengine;

/**
 * Represents a 2-dimensional vector
 */
public class Vector2d extends Vector {

    /**
     * Constructor
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Vector2d(double x, double y) {
        super(x, y);
    }

    /**
     * Default constructor - Creates 0-vector
     */
    public Vector2d() {
        super(0, 0);
    }

    /**
     * Get the x-coordinate
     * @return The x-coordinate
     */
    public double get_x() {
        return coords[0];
    }

    /**
     * Get the y-coordinate
     * @return The y-coordinate
     */
    public double get_y() {
        return coords[1];
    }

}
