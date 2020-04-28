package ai;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import physicsengine.Vector;
import physicsengine.Vector3d;
import physicsengine.engines.RK4;
import physicsengine.functions.FunctionParserRPN;

import java.sql.Time;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HeuristicBot implements PuttingBot {

    @Override
    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position) {
        Vector3d flagPosition = course.get_flag_position();
        double ballFlagDistance = distanceBetween(ball_position, flagPosition);

        double maxVelocity = course.get_maximum_velocity();

        // First calculate the direction the shot must have and set it's velocity to the maximum one
        Vector3d shotDirection = flagPosition.minus(ball_position).getNormalized();

        PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        sim.set_ball_position(ball_position);
        double stepSize = 10e-4;

        Vector3d shot = shotDirection.getScaled(maxVelocity);

        ShotData bestShot = null;
        double bestTargetDistance = Double.MAX_VALUE;

        double i = 1;

        double currentDistanceToTarget;

        long startTime = System.nanoTime();
        long timeLimit = 7;
        while ((System.nanoTime() - startTime)/10e8 <= timeLimit) {

            ShotData resultingPosition = simulateShot(ball_position, sim, shot, stepSize);

            double ballHoleDistFactor = ballFlagDistance / distanceBetween(ball_position, resultingPosition.getCalculatedPosition());

            Vector3d startResultVector = resultingPosition.getCalculatedPosition().minus(ball_position);
            Vector3d startFlagVector = flagPosition.minus(ball_position);
            double ballHoleAngle = 180/Math.PI * Math.acos(
                    (startResultVector.dot(startFlagVector)) / (startResultVector.magnitude() * startFlagVector.magnitude())
            );
            Vector3d crossProductStartFlagAndStartSimulated = startFlagVector.cross(resultingPosition.getCalculatedPosition());

            shot.scale(Math.pow(ballHoleDistFactor, 1/i));
            shot.rotateYAxis(((crossProductStartFlagAndStartSimulated.get_y() > 0) ? -1 : 1) * ballHoleAngle);

            currentDistanceToTarget = resultingPosition.getDistanceToTarget();

            if (currentDistanceToTarget < bestTargetDistance) {
                bestShot = resultingPosition;
            }

            i += 0.5;

        }

        if (bestShot.getShotVector().magnitude() >= maxVelocity) {
            bestShot.getShotVector().normalize();
            bestShot.getShotVector().scale(maxVelocity);
        }

        return bestShot.getShotVector();
    }

    private ShotData simulateShot(Vector3d ballPos, PuttingSimulator simulator, Vector3d shot, double stepSize) {
        simulator.take_shot(shot, stepSize);
        Vector3d newPos = simulator.get_ball_position();
        simulator.set_ball_position(ballPos);
        return new ShotData(shot, distanceBetween(newPos, simulator.getCourse().get_flag_position()), newPos);
    }

    private double distanceBetween(Vector3d v1, Vector3d v2) {
        return Math.sqrt(Math.pow(v1.get_x() - v2.get_x(), 2)
                + Math.pow(v1.get_z() - v2.get_z(), 2));
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("-0.01*x + 0.006*x^2 + 0.04 * y"), new Vector3d(0, 10));
        course.setMaximumVelocity(10);
        PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        PuttingBot bot = new HeuristicBot();

        Vector3d shot = bot.shot_velocity(course, sim.get_ball_position());

        sim.take_shot(shot, 10e-4);
        System.out.println(sim.get_ball_position());
    }

}
