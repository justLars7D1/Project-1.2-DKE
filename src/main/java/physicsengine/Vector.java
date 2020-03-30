package physicsengine;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Abstract notion of an n-dimensional vector
 */
public abstract class Vector implements Serializable {

    /**
     * The vector coordinates
     */
    protected double[] coords;

    /**
     * Constructor
     * @param coords All coordinates of the vector
     */
    protected Vector(double ... coords) {
        this.coords = coords;
    }

    public void scale(double factor) {
        for (int i = 0; i < coords.length; i++) {
            coords[i] *= factor;
        }
    }

    /**
     * Calculate the magnitude of the vector
     * @return The magnitude
     */
    public double magnitude() {
        double magnitude = 0;
        for (double coord: coords) {
            magnitude += Math.pow(coord, 2);
        }
        magnitude = Math.sqrt(magnitude);
        return magnitude;
    }

    /**
     * Normalize the vector
     */
    public void normalize() {
        double magnitude = magnitude();
        if (magnitude != 0) {
            for (int i = 0; i < coords.length; i++) {
                coords[i] /= magnitude;
            }
        }
    }

    /**
     * Gets the string representation of the vector
     * @return The string representation of the vector
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("(");
        for (int i = 0; i < coords.length; i++) {
            res.append(coords[i]);
            if (i != coords.length-1) res.append(",");
        }
        res.append(")");
        return res.toString();
    }

}
