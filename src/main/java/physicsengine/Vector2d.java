package physicsengine;

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

    /**
     * Return a copy of the normalized version of a vector
     * @return Normalized vector
     */
    public Vector2d getNormalized() {
        Vector2d v = copy();
        v.normalize();
        return v;
    }

    /**
     * Creates and returns a copy of the vector
     * @return The copied vector
     */
    public Vector2d copy() {
        return new Vector2d(get_x(), get_y());
    }

    /**
     * Add to the current x-coordinate
     * @param val The value to add
     */
    public void addX(double val) {
        this.coords[0] += val;
    }

    /**
     * Add to the current y-coordinate
     * @param val The value to add
     */
    public void addY(double val) {
        this.coords[1] += val;
    }

    public Vector2d minus(Vector2d otherVector) {
        return new Vector2d(coords[0] - otherVector.get_x(), coords[1] - otherVector.get_y());
    }

    public Vector2d add(Vector2d otherVector) {
        return new Vector2d(coords[0] + otherVector.get_x(), coords[1] + otherVector.get_y());
    }

    public Vector2d mult(Vector2d otherVector) {
        return new Vector2d(coords[0] * otherVector.get_x(), coords[1] * otherVector.get_y());
    }

    public Vector2d getScaled(double scalingFactor) {
        Vector2d newScaled = copy();
        newScaled.scale(scalingFactor);
        return newScaled;
    }

}
