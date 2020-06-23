package physicsengine.engines;

import gameelements.PuttingCourse;
import physicsengine.PhysicsEngine;
import physicsengine.PhysicsLaws;
import physicsengine.Vector3d;

/**
* Approximates the position and velocity of the ball
* using Runge Kutta 5th order
 */

public class RK5 implements PhysicsEngine {
    private PhysicsLaws physicsLaws;
    private double stepSize;

    private Vector3d ballPosition;
    private Vector3d ballVelocity;

    public RK5(PuttingCourse course) {
        this.physicsLaws = new PhysicsLaws(course);
        this.stepSize = 0.01;
    }

    @Override
    public void set_step_size(double step_size) {
    }

    @Override
    public void approximate() {
        Vector3d pos = ballPosition.copy();
        Vector3d vel = ballVelocity.copy();

        Vector3d[] ODEDerivativesK1 = RK5(pos, vel);
        Vector3d[] ODEDerivativesK2 = RK5(pos.add(ODEDerivativesK1[0].getScaled(stepSize / 4)),
                vel.add(ODEDerivativesK1[1].getScaled(stepSize / 4)));
        Vector3d[] ODEDerivativesK3 = RK5(pos.add(ODEDerivativesK2[0].getScaled(stepSize / 4)),
                vel.add(ODEDerivativesK1[1].getScaled(stepSize / 8).minus(ODEDerivativesK2[1].getScaled(stepSize / 8))));
        Vector3d[] ODEDerivativesK4 = RK5(pos.add(ODEDerivativesK3[0].getScaled(stepSize / 2)),
                vel.minus(ODEDerivativesK2[1].getScaled(stepSize / 2)).add(ODEDerivativesK3[1].getScaled(stepSize)));
        Vector3d[] ODEDerivativesK5 = RK5(pos.add(ODEDerivativesK3[0].getScaled(3 * stepSize / 4)),
                vel.add(ODEDerivativesK1[1].getScaled(3 * stepSize / 16)).add(ODEDerivativesK4[1].getScaled(9 * stepSize / 16)));
        Vector3d[] ODEDerivativesK6 = RK5(pos.add(ODEDerivativesK4[0].getScaled(stepSize)),
                vel.minus(ODEDerivativesK1[1].getScaled(3 * stepSize / 7)).add(ODEDerivativesK2[1].getScaled(2 * stepSize / 7)).add(ODEDerivativesK3[1].getScaled(12 * stepSize / 7)).minus(ODEDerivativesK4[1].getScaled(12 * stepSize / 7)).add(ODEDerivativesK5[1].getScaled(8 * stepSize / 7)));


        Vector3d addToPosition = ODEDerivativesK1[0].getScaled(7)
                .add(ODEDerivativesK3[0].getScaled(32))
                .add(ODEDerivativesK4[0].getScaled(12)).add(ODEDerivativesK5[0].getScaled(32)).add(ODEDerivativesK6[0].getScaled(7)).getScaled(stepSize / 90);

        Vector3d addToVelocity = ODEDerivativesK1[1].getScaled(7)
                .add(ODEDerivativesK3[1].getScaled(32))
                .add(ODEDerivativesK4[1].getScaled(12))
                .add(ODEDerivativesK5[1].getScaled(32))
                .add(ODEDerivativesK6[1].getScaled(7)).getScaled(stepSize / 90);

        ballPosition = ballPosition.add(addToPosition);
        ballVelocity = ballVelocity.add(addToVelocity);

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

    private Vector3d[] RK5(Vector3d position, Vector3d velocity) {
        //Index 0: The velocity at this point
        //Index 1: The acceleration at this point
        Vector3d[] result = new Vector3d[2];
        //The velocity at this position is the velocity
        result[0] = velocity;
        result[1] = physicsLaws.ballAcceleration(position, velocity);
        return result;
    }
}
