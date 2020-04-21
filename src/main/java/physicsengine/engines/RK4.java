package physicsengine.engines;

import gameelements.PuttingCourse;
import physicsengine.PhysicsEngine;
import physicsengine.PhysicsLaws;
import physicsengine.Vector3d;

public class RK4 implements PhysicsEngine {

    private PhysicsLaws physicsLaws;
    private double stepSize;

    private Vector3d ballPosition;
    private Vector3d ballVelocity;

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
        Vector3d pos = ballPosition.copy();
        Vector3d vel = ballVelocity.copy();

        Vector3d[] ODEDerivativesK1 = RK4(pos, vel);
        Vector3d[] ODEDerivativesK2 = RK4(pos.add(ODEDerivativesK1[0].getScaled(stepSize / 2)),
                vel.add(ODEDerivativesK1[1].getScaled(stepSize / 2)));
        Vector3d[] ODEDerivativesK3 = RK4(pos.add(ODEDerivativesK2[0].getScaled(stepSize / 2)),
                vel.add(ODEDerivativesK2[1].getScaled(stepSize / 2)));
        Vector3d[] ODEDerivativesK4 = RK4(pos.add(ODEDerivativesK3[0].getScaled(stepSize)),
                vel.add(ODEDerivativesK3[1].getScaled(stepSize)));

        Vector3d addToPosition = ODEDerivativesK1[0]
                .add(ODEDerivativesK2[0].getScaled(2))
                .add(ODEDerivativesK3[0].getScaled(2))
                .add(ODEDerivativesK4[0]).getScaled(stepSize / 6);

        Vector3d addToVelocity = ODEDerivativesK1[1]
                .add(ODEDerivativesK2[1].getScaled(2))
                .add(ODEDerivativesK3[1].getScaled(2))
                .add(ODEDerivativesK4[1]).getScaled(stepSize / 6);

        ballPosition = ballPosition.add(addToPosition);
        ballVelocity = ballVelocity.add(addToVelocity);

    }

    private Vector3d[] RK4(Vector3d position, Vector3d velocity) {
        //Index 0: The velocity at this point
        //Index 1: The acceleration at this point
        Vector3d[] result = new Vector3d[2];
        //The velocity at this position is the velocity
        result[0] = velocity;
        result[1] = physicsLaws.ballAcceleration(position, velocity);
        return result;
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
