package ai;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import physicsengine.engines.RK4;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.Camera;
import ui.entities.PlayerCamera;
import ui.entities.UIPlayer;

import static java.lang.Math.sqrt;

public class Bot implements PuttingBot{
    private float direction;
    private physicsengine.Vector3d velocity;
    private physicsengine.Vector3d ballPosition;
    private physicsengine.Vector3d flagPosition;
    private physicsengine.Vector3d simulatedBallPosition;
    private PuttingSimulator simulator;
    private PuttingCourse course;

    public Bot(float direction, physicsengine.Vector3d velocity, physicsengine.Vector3d ballPosition, physicsengine.Vector3d flagPosition){
        this.direction = direction;
        this.velocity = velocity;
        this.ballPosition = ballPosition;
        this.flagPosition = flagPosition;
    }

    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position) {
        simulateShot(velocity, simulator);

        while(!reachHole(flagPosition, simulatedBallPosition)) {
            if (undershot(flagPosition, ballPosition,simulator, velocity)){
                updateVelocity(new Vector3d(velocity.get_x()-velocity.get_x()/2,velocity.get_y()-velocity.get_y()/2,velocity.get_z()-velocity.get_z()/2) );
            }
            if (overshot(flagPosition, ballPosition,simulator, velocity)){
                updateVelocity(new Vector3d(velocity.get_x()/2,velocity.get_y()/2,velocity.get_z()/2) );
            }
            if(landInWater(ballPosition, flagPosition, course)){
                setDirection(5, direction);
            }
            else{
                setDirection(1, direction);
            }

        }
        return null;
    }

    private physicsengine.Vector3d simulateShot(physicsengine.Vector3d v, PuttingSimulator simulator){
        simulatedBallPosition = simulator.take_shot(v);

        return simulatedBallPosition;
    }

    private boolean landInWater( physicsengine.Vector3d bP,  physicsengine.Vector3d fP, PuttingCourse course){
        // WE DON'T KNOW HOW TO DO
        // TO COMPLETE
    }

    private float setDirection(float angle, float direction){
        direction+=angle;
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

        PuttingBot bot = new Bot(50, ,course.get_start_position(),course.get_flag_position());

        Vector3d shot = bot.shot_velocity(course, sim.get_ball_position());

        //sim.take_shot(shot, 0.01);
        System.out.println(sim.get_ball_position());
    }


}
