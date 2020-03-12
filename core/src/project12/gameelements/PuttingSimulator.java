package project12.gameelements;

import project12.physicsengine.*;
import project12.physicsengine.engines.EulerSolver;
import project12.physicsengine.engines.VerletSolver;
import project12.physicsengine.functions.CourseFunction;
import project12.physicsengine.functions.Function2d;
import project12.physicsengine.functions.FunctionParser;

/**
 * Represents a simulation of the Crazy Putting! game
 */
class PuttingSimulator {

    /**
     * The game course
     */
    PuttingCourse course;

    /**
     * The physics engine
     */
    PhysicsEngine engine;

    /**
     * The current position of the ball
     */
    Vector2d ballPosition;

    /**
     * Constructor
     * @param course The game course
     * @param engine The physics engine
     */
    public PuttingSimulator(PuttingCourse course, PhysicsEngine engine) {
        this.course = course;
        this.engine = engine;
        this.ballPosition = course.get_start_position();
    }

    /**
     * Sets the ball position
     * @param p The position of the ball
     */
    public void set_ball_position(Vector2d p) {
        this.ballPosition = p;
    }

    /**
     * Gets the ball position
     * @return The ball position
     */
    public Vector2d get_ball_position() {
        return this.ballPosition;
    }

   private static final double GRAVITATIONAL_CONSTANT = 9.81;

    /**
     * Simulate taking a shot
     * @param initial_ball_velocity The initial ball velocity of a shot
     */
    public void take_shot(Vector2d initial_ball_velocity) {

        //Copy the initial velocity of the ball
        Vector2d ballVelocity = initial_ball_velocity.copy();

        //Initialize the physics engine
        engine.setPositionVector(ballPosition);
        engine.setVelocityVector(ballVelocity);

        //Set step size if we're using Euler's or Verlet's solver (works since Verlet extends Euler)
        final double deltaT = Math.pow(10, -5);
        if (engine instanceof EulerSolver) ((EulerSolver)(engine)).set_step_size(deltaT);

        //Course function
        Function2d z = course.get_height();

        //Friction constant
        double friction = course.get_friction_coefficient();

        final double comparisonError = 0.01;
        // Keep computing for a small delta t while the velocity is not 0
        while(ballVelocity.get_x() >= comparisonError || ballVelocity.get_y() >= comparisonError) {

            //If the speed is negative, before evaluating, set it to 0
            if (ballVelocity.get_x() < 0) ballVelocity.addX(-ballVelocity.get_x());
            if (ballVelocity.get_y() < 0) ballVelocity.addY(-ballVelocity.get_y());

            // Calculate acceleration using the given formula
            Vector2d gradient = z.gradient(ballPosition);
            Vector2d normalizedVelocity = ballVelocity.getNormalized();

            double aX = -1 * GRAVITATIONAL_CONSTANT * (gradient.get_x() + (friction * normalizedVelocity.get_x()));
            double aY = -1 * GRAVITATIONAL_CONSTANT * (gradient.get_y() + (friction * normalizedVelocity.get_y()));
            Vector2d accelerationVector = new Vector2d(aX, aY);

            //Set the acceleration vector and approximate the new position and velocity of the ball
            engine.setAccelerationVector(accelerationVector);
            engine.approximate();

        }

        System.out.println(ballPosition);

    }

    public static void main(String[] args) {

        Function2d courseFunction = new FunctionParser("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
        Vector2d flag = new Vector2d(10, 10);

        PuttingCourse course = new PuttingCourse(courseFunction, flag);
        PhysicsEngine engine = new VerletSolver();

        PuttingSimulator simulator = new PuttingSimulator(course, engine);

        //Expected results with CourseFunction():   (7.890192765686016 7.890192765689748)
        //Results with replicated FunctionParser(): (7.890192765686016 7.890192765689748)
        //Problem: it's not as fast as expected for complex functions... Maybe improvements can be made
        Vector2d ballVelocity = new Vector2d(4.273, 4.273);
        simulator.take_shot(ballVelocity);

    }

}

