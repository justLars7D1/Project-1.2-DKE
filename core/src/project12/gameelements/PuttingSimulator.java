package project12.gameelements;

import project12.physicsengine.PhysicsEngine;
import project12.physicsengine.Vector2d;

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

    /**
     * Simulate taking a shot
     * @param initial_ball_velocity The initial ball velocity of a shot
     */
    public void take_shot(Vector2d initial_ball_velocity) {
        // Code for taking the shot (so using the provided physics engine and course)
        Vector2d ballVelocity = initial_ball_velocity.copy();
        final double deltaT = Math.pow(10, -2);
        final double comparisonError = 0.1;
        // Keep computing for a small delta t while the velocity is not 0
        while(ballVelocity.get_x() >= comparisonError && ballVelocity.get_y() >= comparisonError) {
            // Calculate acceleration, etc...
            
        }
    }

}

