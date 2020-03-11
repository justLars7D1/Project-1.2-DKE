package project12.gameelements;

import project12.physicsengine.Function2d;
import project12.physicsengine.Vector2d;

/**
 * Represents a course for the Crazy Putting! game
 */
class PuttingCourse {

    /**
     * Function describing the height of the putting terrain on any (x.y)-coordinate
     */
    Function2d height;

    /**
     * The coordinate of the target
     */
    Vector2d flag;
    /**
     * The radius around the target
     */
    double holeTolerance = 1;

    /**
     * The starting point where the ball is on the beginning of the game
     */
    Vector2d start;

    /**
     * The coefficient of friction of the terrain
     */
    double frictionalCoefficient = 1;

    /**
     * The maximum velocity of the ball
     */
    double maximumVelocity = 5;

    //TODO: Hole tolerance and friction coefficient? Are we allowed to change the constructor?

    /**
     * Constructor
     * @param height Set the height function
     * @param flag Set the target position
     * @param start The starting point of the ball
     */
    public PuttingCourse(Function2d height, Vector2d flag, Vector2d start) {
        this.height = height;
        this.flag = flag;
        this.start = start;
    }

    /**
     * Constructor - set start position to (0,0,z) by default
     * @param height Set the height function
     * @param flag Set the target position
     */
    public PuttingCourse(Function2d height, Vector2d flag) {
        this.height = height;
        this.flag = flag;
        this.start = new Vector2d();
    }

    /**
     * Gets the height function
     * @return The height function
     */
    public Function2d get_height() {
        return this.height;
    }

    /**
     * Gets the flag position
     * @return The flag position
     */
    public Vector2d get_flag_position() {
        return this.flag;
    }

    /**
     * Gets the start position
     * @return The starting position
     */
    public Vector2d get_start_position() {
        return this.start;
    }

    /**
     * Set the frictional coefficient
     * @param friction The coefficient of friction
     */
    public void set_friction_coefficient(double friction) {
        this.frictionalCoefficient = friction;
    }

    /**
     * Set the maximum velocity
     * @param maxVelocity The highest velocity
     */
    public void setMaximumVelocity(double maxVelocity) {
        this.maximumVelocity = maxVelocity;
    }

    /**
     * Set the target radius
     * @param tolerance The hole tolerance
     */
    public void setHoleTolerance(double tolerance) {
        this.holeTolerance = tolerance;
    }

    /**
     * Gets the coefficient of friction
     * @return the frictional coefficient
     */
    public double get_friction_coefficient() {
        return frictionalCoefficient;
    }

    /**
     * Gets the maximum velocity
     * @return The maximum velocity
     */
    public double get_maximum_velocity() {
        return maximumVelocity;
    }

    /**
     * Gets the radius of the target
     * @return The hole tolerance
     */
    public double get_hole_tolerance() {
        return holeTolerance;
    }

}

