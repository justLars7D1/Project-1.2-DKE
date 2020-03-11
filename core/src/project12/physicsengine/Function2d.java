package project12.physicsengine;

/**
 * A collective term of all implemented course functions
 */
public interface Function2d {

    /**
     * Factor for computing the derivative as a limit approaching this variable
     */
    double ACCURACYGRADIENTFACTOR = Math.pow(10, -3);

    /**
     * Evaluates the function
     * @param p The (x,y)-coordinate
     * @return The z-coordinate
     */
    double evaluate(Vector2d p);

    /**
     * Compute the gradient at a point of the function
     * @param p The (x,y)-coordinate
     * @return The gradient
     */
    Vector2d gradient(Vector2d p);

}
