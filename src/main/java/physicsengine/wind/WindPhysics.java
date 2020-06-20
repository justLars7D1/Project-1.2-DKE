package physicsengine.wind;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import ui.GameScreen;

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
    private double currentPosition;

    public double getBallVelocity(double time){
        double ballVelocity = currentVelocity + time*(finalSpeed/ballMass);
        currentVelocity = ballVelocity;
        return ballVelocity;
    }

    public double getBallPosition(double time){
        double ballPosition = currentPosition + time*getBallVelocity(time);
        currentPosition = ballPosition;
        return ballPosition;
    }

    /*
    * PROBLEM:
    * iteration deltaT >> other class
    */

}
