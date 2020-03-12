package project12.physicsengine.engines;

import project12.physicsengine.PhysicsEngine;
import project12.physicsengine.Vector2d;

public class VerletSolver extends EulerSolver implements PhysicsEngine {

    /**
     * Constructor
     * @param stepSize The time step size
     */
    public VerletSolver(double stepSize) {
        super(stepSize);
    }

    /**
     * Constructor without arguments
     */
    public VerletSolver() {
        super(0.01);
    }

    /**
     * Since Verlet's equation is just an extension to Euler's method, we can just add the missing parts to it.
     * This is the extra addition of a factor of acceleration, which is obtained by integrating from the acceleration
     * up to the position, ending up with p[t+h] = p[t] + h*v[t] + 1/2*h^2*a[t]. The first two sums were already
     * added by Euler's method and we're only adding the final "1/2*h^2*a[t]" part of the equation to the position
     */
    @Override
    public void approximatePosition() {
        super.approximatePosition();
        double accelerationAdditionX = 0.5 * Math.pow(stepSize, 2) * accelerationVector.get_x();
        double accelerationAdditionY = 0.5 * Math.pow(stepSize, 2) * accelerationVector.get_y();
        positionVector.addX(accelerationAdditionX);
        positionVector.addY(accelerationAdditionY);
    }

}
