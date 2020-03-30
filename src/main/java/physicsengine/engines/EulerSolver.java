package physicsengine.engines;

import physicsengine.PhysicsEngine;
import physicsengine.Vector2d;

public class EulerSolver implements PhysicsEngine {

    /**
     * The time step size
     */
    protected double stepSize;

    /**
     * The position vector
     */
    protected Vector2d positionVector;
    /**
     * The velocity vector
     */
    protected Vector2d velocityVector;
    /**
     * The acceleration vector
     */
    protected Vector2d accelerationVector;

    /**
     * Constructor
     * @param stepSize The time step size
     */
    public EulerSolver(double stepSize) {
        this.stepSize = stepSize;
    }

    /**
     * Constructor without arguments
     */
    public EulerSolver() {
        this.stepSize = 0.01;
    }

    /**
     * Set the step size
     * @param h The step size
     */
    public void set_step_size(double h) {
        this.stepSize = h;
    }

    /**
     * Sets the position vector
     * @param p The position vector
     */
    @Override
    public void setPositionVector(Vector2d p) {
        this.positionVector = p;
    }

    /**
     * Sets the velocity vector
     * @param v The velocity vector
     */
    @Override
    public void setVelocityVector(Vector2d v) {
        this.velocityVector = v;
    }

    /**
     * Sets the acceleration vector
     * @param a The acceleration vector
     */
    @Override
    public void setAccelerationVector(Vector2d a) {
        this.accelerationVector = a;
    }

    /**
     * Calculates the new coordinates of a position and velocity vector
     */
    @Override
    public void approximate() {
        approximatePosition();
        approximateVelocity();
    }

    /**
     * Calculates the new coordinates of a position vector
     */
    protected void approximatePosition() {
        double valX = this.stepSize * this.velocityVector.get_x();
        double valY = this.stepSize * this.velocityVector.get_y();
        positionVector.addX(valX);
        positionVector.addY(valY);
    }

    /**
     * Calculates the new coordinates of a velocity vector
     */
    private void approximateVelocity() {
        double valX = this.stepSize * this.accelerationVector.get_x();
        double valY = this.stepSize * this.accelerationVector.get_y();
        velocityVector.addX(valX);
        velocityVector.addY(valY);
    }

}
