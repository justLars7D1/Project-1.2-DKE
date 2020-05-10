package ai;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;

import org.joml.Vector3f;
import physicsengine.Vector;
import physicsengine.Vector3d;
import physicsengine.engines.RK4;
import physicsengine.functions.Function2d;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.Camera;
import ui.entities.PlayerCamera;
import ui.entities.UIPlayer;

import java.lang.invoke.VarHandle;

import static java.lang.Math.sqrt;

public class Bot implements PuttingBot{
    private Vector3d direction;
    private physicsengine.Vector3d velocity;
    private physicsengine.Vector3d ballPosition;
    private physicsengine.Vector3d flagPosition;
    private physicsengine.Vector3d simulatedBallPosition;
    private PuttingSimulator simulator;
    private PuttingCourse course;

    public Bot(Vector3d direction, Vector3d velocity, Vector3d ballPosition, Vector3d flagPosition){
        this.direction = direction;
        this.velocity = velocity;
        this.ballPosition = ballPosition;
        this.flagPosition = flagPosition;
    }

    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position) {
        simulateShot(velocity, simulator);

        while(!reachHole(flagPosition, simulatedBallPosition)) {

           while (landInWater(ballPosition, flagPosition, course)){
                setDirection(new Vector3d(3,3,0), direction);

                simulateShot(velocity, simulator);

                if( !landInWater(ballPosition, flagPosition, course)){
                    updateBallPosition(simulatedBallPosition);
                }
            }

           if (undershot(flagPosition, ballPosition,simulator, velocity)){
                updateVelocity(new Vector3d(velocity.get_x()-velocity.get_x()/2,velocity.get_y()-velocity.get_y()/2,velocity.get_z()-velocity.get_z()/2) );
            }
            if (overshot(flagPosition, ballPosition,simulator, velocity)){
                updateVelocity(new Vector3d(velocity.get_x()/2,velocity.get_y()/2,velocity.get_z()/2) );
            }
            else{
                setDirection(new Vector3d(1,1,0), direction);
            }

        }
        return null;
    }

    private Vector3d simulateShot(Vector3d v, PuttingSimulator simulator){
        simulatedBallPosition = simulator.take_shot(v);

        return simulatedBallPosition;
    }

    private boolean landInWater( Vector3d bP,  Vector3d fP, PuttingCourse course){
        Vector3d wayOfTheBall=new Vector3d(bP.get_x()+ fP.get_x(),bP.get_y()+fP.get_y(), bP.get_z() + fP.get_z()) ;
        Function2d z = course.get_height();

        if (z.evaluate(wayOfTheBall) < 0) {
            return true;
        }
        return false;
    }

    private Vector3d setDirection(Vector3d angle, Vector3d direction){
        direction =new Vector3d(angle.get_x()+direction.get_x(), angle.get_y()+direction.get_y(), angle.get_z()+direction.get_z());
        return direction;
    }

    private Vector3d updateBallPosition(Vector3d simulatedBallPosition ){
        this.ballPosition=simulatedBallPosition;
        return ballPosition;
    }

    private boolean reachHole(Vector3d flagPosition, Vector3d simulatedBallPosition){
        if(simulatedBallPosition==flagPosition){
            return true;
        }else{
            return false;
        }
    }

    private Vector3d takeAShot(float d, Vector3d v, Vector3d bP, Vector3d sBP, PuttingSimulator s){
        Vector3d ballInHole= s.take_shot(v);
        return ballInHole;
    }

    private Vector3d updateVelocity(Vector3d v){
        this.velocity=v;
        return this.velocity;
    }

    private boolean overshot(Vector3d flagPosition, Vector3d ballPosition, PuttingSimulator s, Vector3d v){
        Vector3d newPosition = s.take_shot(v);
        double d1= sqrt(Math.pow(flagPosition.get_x()-ballPosition.get_x(), 2)+ Math.pow(flagPosition.get_y()-ballPosition.get_y(), 2));
        double d2= sqrt(Math.pow(newPosition.get_x()-ballPosition.get_x(), 2)+ Math.pow(newPosition.get_y()-ballPosition.get_y(), 2));

        if(d1<d2){
            return true;
        }
        return false;
    }

    private boolean undershot(Vector3d flagPosition, Vector3d ballPosition, PuttingSimulator s, Vector3d v){
        Vector3d newPosition = s.take_shot(v);
        double d1= sqrt(Math.pow(flagPosition.get_x()-ballPosition.get_x(), 2)+ Math.pow(flagPosition.get_y()-ballPosition.get_y(), 2));
        double d2= sqrt(Math.pow(newPosition.get_x()-ballPosition.get_x(), 2)+ Math.pow(newPosition.get_y()-ballPosition.get_y(), 2));

        if(d1>d2){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("-0.01*x + 0.003*x^2 + 0.04 * y"), new Vector3d(0, 10));
        PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));

        PuttingBot bot = new Bot(new Vector3d(course.get_start_position().get_x()-course.get_flag_position().get_x(), course.get_start_position().get_y()-course.get_flag_position().get_y(), course.get_start_position().get_z()-course.get_flag_position().get_z()), new Vector3d(10, 10),course.get_start_position(),course.get_flag_position());

        Vector3d shot = bot.shot_velocity(course, sim.get_ball_position());

        sim.take_shot(shot, 0.01);
        System.out.println(sim.get_ball_position());
    }


}
