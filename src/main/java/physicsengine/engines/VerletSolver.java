package physicsengine.engines;

import gameelements.PuttingCourse;
import physicsengine.PhysicsEngine;
import physicsengine.PhysicsLaws;
import physicsengine.Vector3d;

/**
 * Approximates the position and velocity of the ball
 * using Verlet integration
 */

public class VerletSolver implements PhysicsEngine {

    private PhysicsLaws physicsLaws;
    private double stepSize;

    private Vector3d ballPosition;
    private Vector3d ballVelocity;

    public VerletSolver(PuttingCourse course) {
        this.physicsLaws = new PhysicsLaws(course);
        this.stepSize = 0.01;
    }

    @Override
    public void set_step_size(double step_size) {
        this.stepSize = step_size;
    }

    @Override
    public void approximate() {
        Vector3d acceleration = physicsLaws.ballAcceleration(ballPosition, ballVelocity);
        //x += h*v + 1/2*h^2*a
        ballPosition = ballPosition.add(ballVelocity.getScaled(stepSize)).add(acceleration.getScaled(0.5 * Math.pow(stepSize, 2)));
        ballVelocity = ballVelocity.add(acceleration.getScaled(stepSize));
    }

    public void setBallPosition(Vector3d ballPosition) {
        this.ballPosition = ballPosition;
    }

    public void setBallVelocity(Vector3d ballVelocity) {
        this.ballVelocity = ballVelocity;
    }

    public Vector3d getBallPosition() {
        return ballPosition;
    }

    public Vector3d getBallVelocity() {
        return ballVelocity;
    }
}
