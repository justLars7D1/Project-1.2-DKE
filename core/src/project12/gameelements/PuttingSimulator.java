package project12.gameelements;

import project12.physicsengine.*;
import project12.physicsengine.engines.EulerSolver;
import project12.physicsengine.engines.VerletSolver;
import project12.physicsengine.functions.Function2d;
import project12.physicsengine.functions.FunctionParserRPN;

import java.io.*;

/**
 * Represents a simulation of the Crazy Putting! game
 */
public class PuttingSimulator {

    /**
     * The game course
     */
    private PuttingCourse course;

    /**
     * The physics engine
     */
    private PhysicsEngine engine;

    /**
     * The current position of the ball
     */
    private Vector2d ballPosition;

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
     * Constructor that loads the course from a file
     * @param filePath The course file
     * @param engine The physics engine
     */
    public PuttingSimulator(String filePath, PhysicsEngine engine) {
        loadCourse(filePath);
        this.engine = engine;
        this.ballPosition = course.get_start_position();
    }

    /**
     * Constructor with only the physics engine (course can later be loaded by loadCourse(path)
     * @param engine The physics engine
     */
    public PuttingSimulator(PhysicsEngine engine) {
        this.engine = engine;
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

    /**
     * Simulate taking a shot
     * @param initial_ball_velocity The initial ball velocity of a shot
     */
    public void take_shot(Vector2d initial_ball_velocity) {

        //Copy the initial velocity of the ball
        Vector2d ballVelocity = initial_ball_velocity.copy();
        Vector2d accelerationVector = new Vector2d(1, 1);

        //Initialize the physics engine
        engine.setPositionVector(ballPosition);
        engine.setVelocityVector(ballVelocity);

        //Set step size if we're using Euler's or Verlet's solver (works since Verlet extends Euler)
        final double deltaT = Math.pow(10, -3);
        if (engine instanceof EulerSolver) ((EulerSolver)(engine)).set_step_size(deltaT);

        //Course function
        Function2d z = course.get_height();

        //Friction constant
        double friction = course.get_friction_coefficient();

        while((!isZero(ballVelocity.get_x()) || !isZero(accelerationVector.get_x())) || (!isZero(ballVelocity.get_y()) || !isZero(accelerationVector.get_y()))) {

            if (isZero(ballVelocity.get_x())) ballVelocity.addX(-ballVelocity.get_x());
            if (isZero(ballVelocity.get_y())) ballVelocity.addY(-ballVelocity.get_y());

            // Calculate acceleration using the given formula
            Vector2d gradient = z.gradient(ballPosition);
            Vector2d normalizedVelocity = ballVelocity.getNormalized();

            double aX = -1 * course.get_gravitational_constant() * (gradient.get_x() + (friction * normalizedVelocity.get_x()));
            double aY = -1 * course.get_gravitational_constant() * (gradient.get_y() + (friction * normalizedVelocity.get_y()));
            accelerationVector = new Vector2d(aX, aY);

            //Set the acceleration vector and approximate the new position and velocity of the ball
            engine.setAccelerationVector(accelerationVector);
            engine.approximate();

        }

    }

    private boolean isZero(double x) {
        final double comparisonError = 0.1;
        return -comparisonError <= x && x <= comparisonError;
    }

    /**
     * Saves the course to a file denoted by the specified filepath
     * @param filePath The filepath
     * @return Whether the operation was successfull or not
     */
    public boolean saveCourse(String filePath) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream outputStream = new ObjectOutputStream(f);
            outputStream.writeObject(course);
            outputStream.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Loads the course from a file denoted by the specified filepath
     * @param filePath The filepath
     * @return Whether the operation was successfull or not
     */
    public boolean loadCourse(String filePath) {
        try {
            FileInputStream f = new FileInputStream(filePath);
            ObjectInputStream inputStream = new ObjectInputStream(f);
            course = (PuttingCourse) (inputStream.readObject());
            this.ballPosition = course.get_start_position();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public PuttingCourse getCourse() {
        return course;
    }

    public static void main(String[] args) {

        Function2d courseFunction = new FunctionParserRPN("-1*(x+y)");
        Vector2d flag = new Vector2d(10, 10);

        PuttingCourse course = new PuttingCourse(courseFunction, flag);
        PhysicsEngine engine = new VerletSolver();

        PuttingSimulator simulator = new PuttingSimulator(course, engine);

        Vector2d ballVelocity = new Vector2d(4.273, 4.273);
        simulator.take_shot(ballVelocity);

        System.out.println(simulator.get_ball_position());

    }

}

