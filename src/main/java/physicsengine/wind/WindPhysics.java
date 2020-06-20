package physicsengine.wind;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;

public class WindPhysics {

    private double gravity = 9.8;
    private double ballMass = PuttingCourse.get_ball_mass()
    private double ballForce = gravity*ballMass;
    private double windSpeed;
    private double ballSpeed;
    private double ballConstant=0.2;
    private double windForce = ballConstant*(ballSpeed-windSpeed);
    private double finalSpeed = ballForce + windForce;
    private double currentVelocity;
    private double currentPosition;

    public double getBallVelocity(double time){
        double ballVelocity = currentVelocity + time*((windForce+ballForce)/ballMass);
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
    * getters
    */

}
