package gameelements;

import org.joml.Vector3f;
import physicsengine.PhysicsEngine;
import physicsengine.Vector2d;
import physicsengine.engines.EulerSolver;
import physicsengine.engines.VerletSolver;
import physicsengine.functions.Function2d;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.UIPlayer;
import ui.status.StatusMessage;

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
     * Returns whether the shot reached the target (game ended)
     * @param p The position of the golf ball
     * @return Whether or not the shot reached the target
     */
    public boolean holeReached(Vector2d p) {
        Vector2d flag = course.get_flag_position();
        double holeTolerance = course.get_hole_tolerance();
        return (flag.get_x()-holeTolerance <= p.get_x() && p.get_x() <= flag.get_x()+holeTolerance)
                && (flag.get_y()-holeTolerance <= p.get_y() && p.get_y() <= flag.get_y()+holeTolerance);
    }

    /**
     * Simulate taking a shot, updating the ball live
     * @param initial_ball_velocity The initial ball velocity of a shot
     */
    public void take_shot(Vector2d initial_ball_velocity) {

        //Copy the initial velocity and position of the ball
        Vector2d ballVelocity = initial_ball_velocity.copy();
        Vector2d accelerationVector;

        //Initialize the physics engine
        engine.setPositionVector(ballPosition);
        engine.setVelocityVector(ballVelocity);

        //Set step size if we're using Euler's or Verlet's solver (works since Verlet extends Euler)
        final double deltaT = Math.pow(10, -4);
        if (engine instanceof EulerSolver) ((EulerSolver)(engine)).set_step_size(deltaT);

        //Course function
        Function2d z = course.get_height();
        Vector2d gradient;

        int numTimesCloseToCurrent = 0;
        Vector2d current = ballPosition.copy();

        while(numTimesCloseToCurrent < 100) {

            // Calculate acceleration using the given formula
            gradient = z.gradient(ballPosition);

            //Get the acceleration at the current point by applying the laws of Physics
            accelerationVector = ballAcceleration(gradient, ballVelocity);

            //Set the acceleration vector and approximate the new position and velocity of the ball
            engine.setAccelerationVector(accelerationVector);
            engine.approximate();

            if (Math.abs(current.get_x() - ballPosition.get_x()) <= Math.pow(10,-5) && Math.abs(current.get_y() - ballPosition.get_y()) <= Math.pow(10,-5)) {
                numTimesCloseToCurrent++;
            } else {
                numTimesCloseToCurrent = 0;
            }
            current = ballPosition.copy();

        }

    }

    /**
     * Simulate taking a shot, updating the ball live
     * @param initial_ball_velocity The initial ball velocity of a shot
     * @param player The player that takes the shot
     */
    public void take_shot(Vector2d initial_ball_velocity, UIPlayer player) {

        //Copy the initial velocity and position of the ball
        Vector2d ballVelocity = initial_ball_velocity.copy();
        Vector2d accelerationVector;

        //Initialize the physics engine
        engine.setPositionVector(ballPosition);
        engine.setVelocityVector(ballVelocity);

        //Set step size if we're using Euler's or Verlet's solver (works since Verlet extends Euler)
        final double deltaT = Math.pow(10, -4);
        if (engine instanceof EulerSolver) ((EulerSolver)(engine)).set_step_size(deltaT);

        //Course function
        Function2d z = course.get_height();
        Vector2d gradient;

        //Friction constant
        double friction = course.get_friction_coefficient();

        int numTimesCloseToCurrent = 0;
        Vector2d current = ballPosition.copy();

        while(numTimesCloseToCurrent < 100) {

            // Calculate acceleration using the given formula
            gradient = z.gradient(ballPosition);

            //Get the acceleration at the current point by applying the laws of Physics
            accelerationVector = ballAcceleration(gradient, ballVelocity);

            //Set the acceleration vector and approximate the new position and velocity of the ball
            engine.setAccelerationVector(accelerationVector);
            engine.approximate();

            if (Math.abs(current.get_x() - ballPosition.get_x()) <= Math.pow(10,-5) && Math.abs(current.get_y() - ballPosition.get_y()) <= Math.pow(10,-5)) {
                numTimesCloseToCurrent++;
            } else {
                numTimesCloseToCurrent = 0;
            }
            current = ballPosition.copy();

            Vector3f newPos = new Vector3f((float)ballPosition.get_x(), (float)z.evaluate(ballPosition), (float)ballPosition.get_y());
            player.setPosition(newPos);

        }

        Vector3f newPos = new Vector3f((float)ballPosition.get_x(), (float)z.evaluate(ballPosition), (float)ballPosition.get_y());
        player.setPosition(newPos);

        boolean isInHole = Math.sqrt(Math.pow(ballPosition.get_x() - course.get_flag_position().get_x(), 2) +
                                     Math.pow(ballPosition.get_y() - course.get_flag_position().get_y(), 2)) <= course.get_hole_tolerance();

        if (z.evaluate(ballPosition) < 0) {
            StatusMessage.setSTATUS("water");
        } else if (isInHole) {
            StatusMessage.setSTATUS("finished");
        } else {
            StatusMessage.setSTATUS("success");
        }

    }

    private Vector2d ballAcceleration(Vector2d fieldGradient, Vector2d ballVelocity) {
        Vector2d gravitationalForce = gravitationalForce(fieldGradient);
        Vector2d frictionalForce = frictionalForce(ballVelocity);
        Vector2d totalAcceleration = gravitationalForce.add(frictionalForce);
        totalAcceleration.scale(1/course.get_ball_mass());
        return totalAcceleration;
    }

    private Vector2d gravitationalForce(Vector2d fieldGradient) {
        double constantPart = -1 * course.get_ball_mass() * course.get_gravitational_constant();
        return new Vector2d(constantPart * fieldGradient.get_x(), constantPart * fieldGradient.get_y());
    }

    private Vector2d frictionalForce(Vector2d ballVelocity) {
        double constantPart = -1 * course.get_friction_coefficient() * course.get_ball_mass() * course.get_gravitational_constant();
        Vector2d normalizedVelocity = ballVelocity.getNormalized();
        normalizedVelocity.scale(constantPart);
        return normalizedVelocity;
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
            Object courseObj = inputStream.readObject();
            if (courseObj instanceof PuttingCourse) {
                course = (PuttingCourse) (courseObj);
                inputStream.close();
                this.ballPosition = course.get_start_position();
            } else {
                inputStream.close();
                loadCourseByString(filePath);
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean loadCourseByString(String filePath) {
    	Input load = new Input(filePath);
    	course = load.loadCourse(filePath);
    	if (course != null) {
    		this.ballPosition = course.get_start_position();
    		return true;
    	}
    	return false;
    }

    public PuttingCourse getCourse() {
        return course;
    }

    public PuttingSimulator copy() {
        PuttingCourse course = this.course.copy();
        PuttingSimulator simulator = new PuttingSimulator(course, engine);
        simulator.set_ball_position(ballPosition.copy());
        return simulator;
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("-0.01*x + 0.003*x^2 + 0.04 * y"), new Vector2d(0, 10));
        PuttingSimulator sim = new PuttingSimulator(course, new VerletSolver());
        sim.take_shot(new Vector2d(0, 3));
        System.out.println(sim.get_ball_position());
    }

}

