package physicsengine.engines;

import gameelements.PuttingCourse;
import org.lwjgl.odbc.SQL_YEAR_MONTH_STRUCT;
import physicsengine.PhysicsEngine;
import physicsengine.PhysicsLaws;
import physicsengine.Vector2d;

public class EulerSolver implements PhysicsEngine {

    private PhysicsLaws physicsLaws;
    private double stepSize;

    private Vector2d ballPosition;
    private Vector2d ballVelocity;

    public EulerSolver(PuttingCourse course) {
        this.physicsLaws = new PhysicsLaws(course);
        this.stepSize = 0.01;
    }

    @Override
    public void set_step_size(double step_size) {
        this.stepSize = step_size;
    }

    @Override
    public void approximate() {
        Vector2d acceleration = physicsLaws.ballAcceleration(ballPosition, ballVelocity);
        ballPosition = ballPosition.add(ballVelocity.getScaled(stepSize));
        ballVelocity = ballVelocity.add(acceleration.getScaled(stepSize));
    }

    public void setBallPosition(Vector2d ballPosition) {
        this.ballPosition = ballPosition;
    }

    public void setBallVelocity(Vector2d ballVelocity) {
        this.ballVelocity = ballVelocity;
    }

    public Vector2d getBallPosition() {
        return ballPosition;
    }

    public Vector2d getBallVelocity() {
        return ballVelocity;
    }
}
