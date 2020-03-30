package physicsengine.engines;

import gameelements.PuttingCourse;
import physicsengine.PhysicsEngine;
import physicsengine.PhysicsLaws;
import physicsengine.Vector2d;

public class RK4 implements PhysicsEngine {

    private PhysicsLaws physicsLaws;
    private double stepSize;

    private Vector2d ballPosition;
    private Vector2d ballVelocity;

    public RK4(PuttingCourse course) {
        this.physicsLaws = new PhysicsLaws(course);
        this.stepSize = 0.01;
    }

    @Override
    public void set_step_size(double step_size) {
        this.stepSize = step_size;
    }

    @Override
    public void approximate() {
        Vector2d pos = ballPosition.copy();
        Vector2d vel = ballVelocity.copy();
        Vector2d[] ODEDerivativesK1 = RK4(pos, vel);
        Vector2d[] ODEDerivativesK2 = RK4(pos.add(ODEDerivativesK1[0].getScaled(stepSize / 2)),
                vel.add(ODEDerivativesK1[1].getScaled(stepSize / 2)));
        Vector2d[] ODEDerivativesK3 = RK4(pos.add(ODEDerivativesK2[0].getScaled(stepSize / 2)),
                vel.add(ODEDerivativesK2[1].getScaled(stepSize / 2)));
        Vector2d[] ODEDerivativesK4 = RK4(pos.add(ODEDerivativesK3[0].getScaled(stepSize)),
                vel.add(ODEDerivativesK3[1].getScaled(stepSize)));

        Vector2d addToPosition = ODEDerivativesK1[0]
                .add(ODEDerivativesK2[0].getScaled(2))
                .add(ODEDerivativesK3[0].getScaled(2))
                .add(ODEDerivativesK4[0]).getScaled(stepSize / 6);

        Vector2d addToVelocity = ODEDerivativesK1[1]
                .add(ODEDerivativesK2[1].getScaled(2))
                .add(ODEDerivativesK3[1].getScaled(2))
                .add(ODEDerivativesK4[1]).getScaled(stepSize / 6);

        ballPosition = ballPosition.add(addToPosition);
        ballVelocity = ballVelocity.add(addToVelocity);

    }

    private Vector2d[] RK4(Vector2d position, Vector2d velocity) {
        //Index 0: The velocity at this point
        //Index 1: The acceleration at this point
        Vector2d[] result = new Vector2d[2];
        //The velocity at this position is the velocity
        result[0] = velocity;
        result[1] = physicsLaws.ballAcceleration(position, velocity);
        return result;
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
