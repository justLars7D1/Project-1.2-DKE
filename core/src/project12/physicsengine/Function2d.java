package project12.physicsengine;

public interface Function2d {

    /**
     * Factor for computing the derivative as a limit approaching this variable
     */
    static final double ACCURACYGRADIENTFACTOR = Math.pow(10, -3);

    double evaluate(Vector2d p);
    Vector2d gradient(Vector2d p);
}
