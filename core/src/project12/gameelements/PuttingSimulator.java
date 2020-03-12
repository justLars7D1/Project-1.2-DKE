package project12.gameelements;

import project12.physicsengine.*;

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

        final double deltaT = Math.pow(10, -3);
        if (engine instanceof EulerSolver) ((EulerSolver)(engine)).set_step_size(deltaT);

        //Course function
        Function2d z = course.get_height();

        //Friction constant
        double friction = course.get_friction_coefficient();

        final double comparisonError = 0.1;
        // Keep computing for a small delta t while the velocity is not 0
        while(ballVelocity.get_x() >= comparisonError || ballVelocity.get_y() >= comparisonError) {

            // Calculate acceleration using the given formula
            Vector2d gradient = z.gradient(ballPosition);
            Vector2d normalizedVelocity = ballVelocity.getNormalized();

            double aX = GRAVITATIONAL_CONSTANT * (gradient.get_x() - (friction * normalizedVelocity.get_x()));
            double aY = GRAVITATIONAL_CONSTANT * (gradient.get_y() - (friction * normalizedVelocity.get_y()));
            Vector2d accelerationVector = new Vector2d(aX, aY);

            //Set the acceleration vector and approximate the new position and velocity of the ball
            engine.setAccelerationVector(accelerationVector);
            engine.approximate();

            System.out.println(ballPosition);

        }
    }

    public static void main(String[] args) {

        Function2d courseFunction = new CourseFunction();
        Vector2d flag = new Vector2d(10, 10);

        PuttingCourse course = new PuttingCourse(courseFunction, flag);
        PhysicsEngine engine = new EulerSolver();

        PuttingSimulator simulator = new PuttingSimulator(course, engine);

        Vector2d ballVelocity = new Vector2d(5, 10);
        simulator.take_shot(ballVelocity);

    }

}

