package gameelements;

import org.joml.Vector3f;
import physicsengine.PhysicsEngine;
import physicsengine.Vector;
import physicsengine.Vector3d;
import physicsengine.engines.EulerSolver;
import physicsengine.engines.RK4;
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
    private Vector3d ballPosition;

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

    public PuttingSimulator() {}

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
    public void set_ball_position(Vector3d p) {
        this.ballPosition = p;
    }

    /**
     * Gets the ball position
     * @return The ball position
     */
    public Vector3d get_ball_position() {
        return this.ballPosition;
    }

    /**
     * Simulate taking a shot (used for the bot)
     * @param initial_ball_velocity The initial ball velocity of a shot
     */
    public void take_shot(Vector3d initial_ball_velocity, double deltaT) {

        //Copy the initial velocity and position of the ball
        Vector3d ballVelocity = initial_ball_velocity.copy();

        //Initialize the physics engine
        engine.set_step_size(deltaT);
        engine.setBallPosition(ballPosition);
        engine.setBallVelocity(ballVelocity);

        Function2d z = course.get_height();

        int numTimesCloseToCurrent = 0;
        Vector3d current = ballPosition.copy();
        boolean ballHigherThanGround = false;
        boolean ballLastInAir;
        Vector3d lastBouncePosition = new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        while(numTimesCloseToCurrent < 100 || ballHigherThanGround) {

            engine.approximate();
            ballPosition = engine.getBallPosition();
            ballVelocity = engine.getBallVelocity();

            if (Math.abs(current.get_x() - ballPosition.get_x()) <= deltaT*0.1 && Math.abs(current.get_z() - ballPosition.get_z()) <= deltaT*0.1) {
                numTimesCloseToCurrent++;
            } else {
                numTimesCloseToCurrent = 0;
            }
            ballLastInAir = ballHigherThanGround;
            ballHigherThanGround = ballPosition.get_y() >= z.evaluate(ballPosition);

            double curHeight = z.evaluate(ballPosition);
            if (ballPosition.get_y() < curHeight) ballPosition.set_y(curHeight);

            double bounceDistance = Math.sqrt(Math.pow(lastBouncePosition.get_x() - ballPosition.get_x(), 2) +
                    Math.pow(lastBouncePosition.get_z() - ballPosition.get_z(), 2));
            if (ballLastInAir && !ballHigherThanGround && bounceDistance > 0.2) {
                lastBouncePosition = ballPosition.copy();
                engine.setBallVelocity(reflectionVectorOnBounce(ballVelocity, z.gradient(ballPosition)));
            }

            current = ballPosition.copy();

            System.out.println(ballPosition);

            if (isInWater()) {
                break;
            }

        }

    }

    private static Vector3d reflectionVectorOnBounce(Vector3d velocityVector, Vector3d fieldGradient) {
        Vector3d normalizedGradient = fieldGradient.getNormalized();
        double dotResult = 2*velocityVector.dot(normalizedGradient);
        return velocityVector.minus(normalizedGradient.getScaled(dotResult)).getScaled(PuttingCourse.getBallBounciness());
    }

    /**
     * Simulate taking a shot, updating the ball live
     * @param initial_ball_velocity The initial ball velocity of a shot
     * @param player The player that takes the shot
     */
    public void take_shot(Vector3d initial_ball_velocity, UIPlayer player) {

        //Copy the initial velocity and position of the ball
        Vector3d ballVelocity = initial_ball_velocity.copy();

        //Initialize the physics engine
        final double deltaT = 0.01;
        engine.set_step_size(deltaT);
        engine.setBallPosition(ballPosition);
        engine.setBallVelocity(ballVelocity);

        //Course function
        Function2d z = course.get_height();

        int numTimesCloseToCurrent = 0;
        Vector3d current = ballPosition.copy();
        boolean ballHigherThanGround = false;
        boolean ballLastInAir;
        Vector3d lastBouncePosition = new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        while(numTimesCloseToCurrent < 100 || ballHigherThanGround) {

            engine.approximate();
            ballPosition = engine.getBallPosition();
            ballVelocity = engine.getBallVelocity();

            if (Math.abs(current.get_x() - ballPosition.get_x()) <= deltaT*0.1 && Math.abs(current.get_z() - ballPosition.get_z()) <= deltaT*0.1) {
                numTimesCloseToCurrent++;
            } else {
                numTimesCloseToCurrent = 0;
            }
            ballLastInAir = ballHigherThanGround;
            ballHigherThanGround = ballPosition.get_y() >= z.evaluate(ballPosition);

            double curHeight = z.evaluate(ballPosition);
            if (ballPosition.get_y() < curHeight) ballPosition.set_y(curHeight);

            double bounceDistance = Math.sqrt(Math.pow(lastBouncePosition.get_x() - ballPosition.get_x(), 2) +
                    Math.pow(lastBouncePosition.get_z() - ballPosition.get_z(), 2));
            if (ballLastInAir && !ballHigherThanGround && bounceDistance > 0.2) {
                lastBouncePosition = ballPosition.copy();
                engine.setBallVelocity(reflectionVectorOnBounce(ballVelocity, z.gradient(ballPosition)));
            }

            current = ballPosition.copy();

            Vector3f newPos = new Vector3f((float)ballPosition.get_x(), (float)ballPosition.get_y(), (float)ballPosition.get_z());
            player.setPosition(newPos);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isInWater()) {
                StatusMessage.setSTATUS("water");
                break;
            }

        }

        Vector3f newPos = new Vector3f((float)ballPosition.get_x(), (float)z.evaluate(ballPosition), (float)ballPosition.get_z());
        player.setPosition(newPos);

        boolean inWater = isInWater();
        if (!inWater && isInHole()) {
            StatusMessage.setSTATUS("finished");
        } else if(!inWater) {
            StatusMessage.setSTATUS("success");
        }

    }

    public boolean isInHole() {
        return  Math.sqrt(Math.pow(ballPosition.get_x() - course.get_flag_position().get_x(), 2) +
                Math.pow(ballPosition.get_z() - course.get_flag_position().get_z(), 2)) <= course.get_hole_tolerance();
    }

    public boolean isInWater() {
        return course.get_height().evaluate(ballPosition) < 0 && ballPosition.get_y() < 0;
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
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("5*sin(0.1*x)*sin(0.1*y)+1"), new Vector3d(0, 10));
        PuttingSimulator sim3 = new PuttingSimulator(course.copy(), new RK4(course.copy()));
        sim3.take_shot(new Vector3d(20, 0, 20), 0.01); //(0.08833984711499843,0.0,2.684909583202568)
        System.out.println("RK4: " + sim3.get_ball_position());
    }


}

