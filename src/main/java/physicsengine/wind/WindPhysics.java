package physicsengine.wind;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import ui.GameScreen;

@Deprecated
public class WindPhysics {

    private double gravity = 9.8;
    private PuttingCourse puttingCourse;
    private double ballMass = puttingCourse.get_ball_mass();
    private double ballForce = gravity*ballMass;
    private Wind wind;
    private double windSpeed = wind.getVelocity();
    private GameScreen gs;
    private double ballSpeed = gs.getBallVelocity();
    private double ballConstant = 0.2;
    private double windForce = ballConstant*(ballSpeed-windSpeed);
    private double finalSpeed = ballForce + windForce;
    private double currentVelocity;
    private Vector3d currentPosition;

    public double getBallVelocity(double time){
        double ballVelocity = currentVelocity + time*(finalSpeed/ballMass);
        currentVelocity = ballVelocity;
        return ballVelocity;
    }

    public Vector3d getBallPosition(double time){
        Vector3d ballPosition = currentPosition;
        ballPosition.set_x(ballPosition.get_x() + time*getBallVelocity(time));
        ballPosition.set_y(ballPosition.get_y() + time*getBallVelocity(time));
        ballPosition.set_z(ballPosition.get_z() + time*getBallVelocity(time));
        currentPosition = ballPosition;
        return ballPosition;
    }

}
