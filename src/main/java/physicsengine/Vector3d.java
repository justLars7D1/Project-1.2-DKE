package physicsengine;

/**
 * Represents a 2-dimensional vector
 */
public class Vector3d extends Vector {

    /**
     * Constructor
     * @param x The x-coordinate
     * @param z The z-coordinate
     */
    public Vector3d(double x, double z) {
        super(x, 0, z);
    }

    /**
     * Constructor
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    public Vector3d(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Default constructor - Creates 0-vector
     */
    public Vector3d() {
        super(0, 0, 0);
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
     * Get the z-coordinate
     * @return The z-coordinate
     */
    public double get_z() {
        return coords[2];
    }

    /**
     * Set the y-coordinate (height)
     * @param x The new y-coordinate
     */
    public void set_x(double x) {
        this.coords[1] = x;
    }

    /**
     * Set the y-coordinate (height)
     * @param y The new y-coordinate
     */
    public void set_y(double y) {
        this.coords[1] = y;
    }

    /**
     * Set the y-coordinate (height)
     * @param z The new y-coordinate
     */
    public void set_z(double z) {
        this.coords[1] = z;
    }

    /**
     * Return a copy of the normalized version of a vector
     * @return Normalized vector
     */
    public Vector3d getNormalized() {
        Vector3d v = copy();
        v.normalize();
        return v;
    }

    /**
     * Creates and returns a copy of the vector
     * @return The copied vector
     */
    public Vector3d copy() {
        return new Vector3d(get_x(), get_y(), get_z());
    }

    /**
     * Subtracts a vector from this vector
     * @param otherVector The vector to subtract
     * @return A new vector containing the result
     */
    public Vector3d minus(Vector3d otherVector) {
        return new Vector3d(coords[0] - otherVector.get_x(), coords[1] - otherVector.get_y(), coords[2] - otherVector.get_z());
    }

    /**
     * Adds a vector to this vector
     * @param otherVector The vector to add
     * @return A new vector containing the result
     */
    public Vector3d add(Vector3d otherVector) {
        return new Vector3d(coords[0] + otherVector.get_x(), coords[1] + otherVector.get_y(), coords[2] + otherVector.get_z());
    }

    /**
     * Scales this vector by a factor
     * @param scalingFactor The factor to scale the vector by
     * @return The scaled vector
     */
    public Vector3d getScaled(double scalingFactor) {
        Vector3d newScaled = copy();
        newScaled.scale(scalingFactor);
        return newScaled;
    }

    /**
     * Returns a new rotated vector around the Y-axis
     * @param theta The angle of rotation
     * @return The rotated vector
     */
    public Vector3d getRotatedYAxis(double theta) {
        Vector3d newRotated = copy();
        newRotated.rotateYAxis(theta);
        return newRotated;
    }

    /**
     * Rotates a 3d-vector around the y-axis
     * @param theta The angle of rotation
     */
    public void rotateYAxis(double theta) {
        theta = Math.toRadians(theta);
        double tmpX = get_x(), tmpZ = get_z();
        coords[0] = Math.cos(theta)*tmpX + Math.sin(theta)*tmpZ;
        coords[2] = -1*Math.sin(theta)*tmpX + Math.cos(theta)*tmpZ;
    }

    /**
     * Calculate the cross-product with another vector
     * @param v the other vector to use in the calculation
     * @return the cross product
     */
    public Vector3d cross(Vector3d v) {
        double crossX = coords[0] * v.coords[0]
                - coords[2] * v.coords[1];
        double crossY = coords[2] * v.coords[0]
                - coords[0] * v.coords[2];
        double crossZ = coords[0] * v.coords[1]
                - coords[1] * v.coords[0];
        return new Vector3d(crossX, crossY, crossZ);
    }

    /**
     * Calculates the angle of a vector
     * @param otherVector : a Vector3d
     * @return the angle
     */
    public double calculateAngle(Vector3d otherVector) {
        double dotProd = dot(otherVector);
        double multipliedMagnitude = magnitude() * otherVector.magnitude();
        double cosTheta = dotProd/multipliedMagnitude;
        return Math.acos(cosTheta);
    }

}
