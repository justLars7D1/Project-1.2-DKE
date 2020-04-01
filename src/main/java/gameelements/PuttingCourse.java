package gameelements;

import physicsengine.functions.Function2d;
import physicsengine.Vector3d;

import java.io.Serializable;

/**
 * Represents a course for the Crazy Putting! game
 */
public class PuttingCourse implements Serializable {

    /**
     * Function describing the height of the putting terrain on any (x.y)-coordinate
     */
    private Function2d height;

    /**
     * The coordinate of the target
     */
    private Vector3d flag;
    /**
     * The radius around the target
     */
    private double holeTolerance = 0.02;

    /**
     * The starting point where the ball is on the beginning of the game
     */
    private Vector3d start;

    /**
     * The coefficient of friction of the terrain
     */
    private double frictionalCoefficient = 0.131;

    /**
     * The maximum velocity of the ball
     */
    private double maximumVelocity = 3;

    /**
     * The gravitational constant
     */
    private double gravitationalConstant = 9.81;

    /**
     * The mass of the golf ball
     */
    private double ballMass = 45.93;

    /**
     * Constructor
     * @param height Set the height function
     * @param flag Set the target position
     * @param start The starting point of the ball
     */
    public PuttingCourse(Function2d height, Vector3d start, Vector3d flag) {
        this.height = height;
        this.flag = flag;
        this.start = start;
    }

    /**
     * Constructor - set start position to (0,0,z) by default
     * @param height Set the height function
     * @param flag Set the target position
     */
    public PuttingCourse(Function2d height, Vector3d flag) {
        this.height = height;
        this.flag = flag;
        this.start = new Vector3d();
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
    public Vector3d get_flag_position() {
        return this.flag;
    }

    /**
     * Gets the start position
     * @return The starting position
     */
    public Vector3d get_start_position() {
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
     * Set the gravitational constant (in m/s^2)
     * @param gConstant The gravitational constant
     */
    public void setGravitationalConstant(double gConstant) {
        this.gravitationalConstant = gConstant;
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

    /**
     * Gets the gravitational constant
     * @return The gravitational constant
     */
    public double get_gravitational_constant() {
        return gravitationalConstant;
    }

    /**
     * Gets the mass of the golf ball
     * @return The mass of the golf ball
     */
    public double get_ball_mass() {
        return ballMass;
    }

    /**
     * Sets the mass of the golf ball
     * @param ballMass The mass of the golf ball
     */
    public void setBallMass(double ballMass) {
        this.ballMass = ballMass;
    }

    /**
     * Gets the string representation of the course
     * @return String representation of the course
     */
    @Override
    public String toString() {
        return "PuttingCourse{" +
                "height=" + height +
                ", flag=" + flag +
                ", holeTolerance=" + holeTolerance +
                ", start=" + start +
                ", frictionalCoefficient=" + frictionalCoefficient +
                ", maximumVelocity=" + maximumVelocity +
                ", gravitationalConstant=" + gravitationalConstant +
                '}';
    }

    public PuttingCourse copy() {
        PuttingCourse course = new PuttingCourse(height, start.copy(), flag.copy());
        course.setMaximumVelocity(maximumVelocity);
        course.setGravitationalConstant(gravitationalConstant);
        course.setBallMass(ballMass);
        course.setHoleTolerance(holeTolerance);
        course.set_friction_coefficient(frictionalCoefficient);
        return course;
    }

}

