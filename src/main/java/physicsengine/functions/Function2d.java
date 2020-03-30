package physicsengine.functions;

import physicsengine.Vector2d;

import java.io.Serializable;

/**
 * A collective term of all implemented course functions
 */
public interface Function2d extends Serializable {

    /**
     * Factor for computing the derivative as a limit approaching this variable
     */
//    double ACCURACYGRADIENTFACTOR = Math.pow(10, -1);

    /**
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    double evaluate(Vector2d p);

    /**
     * Evaluates the function
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The z-coordinate
     */
    double evaluate(double x, double y);

    /**
     * Compute the partial derivative with respect to x
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The value of the partial derivative with respect to x at that point
     */
    double partialDerivativeX(double x, double y);

    /**
     * Compute the partial derivative with respect to y
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The value of the partial derivative with respect to y at that point
     */
    double partialDerivativeY(double x, double y);

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    Vector2d gradient(Vector2d p);

}
