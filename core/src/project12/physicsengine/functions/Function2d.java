package project12.physicsengine.functions;

import project12.physicsengine.Vector2d;

import java.io.Serializable;

/**
 * A collective term of all implemented course functions
 */
public interface Function2d extends Serializable {

    /**
     * Factor for computing the derivative as a limit approaching this variable
     */
    double ACCURACYGRADIENTFACTOR = Math.pow(10, -2);

    /**
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    double evaluate(Vector2d p);

    double evaluate(double x, double y);

    double partialDerivativeX(double x, double y);
    double partialDerivativeY(double x, double y);

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    Vector2d gradient(Vector2d p);

}
