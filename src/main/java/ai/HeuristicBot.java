package ai;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import physicsengine.Vector;
import physicsengine.Vector3d;
import physicsengine.engines.RK4;
import physicsengine.functions.FunctionParserRPN;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HeuristicBot implements PuttingBot {

    @Override
    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position) {
        Vector3d flagPosition = course.get_flag_position();
        double ballFlagDistance = distanceBetween(ball_position, flagPosition);

        double maxVelocity = course.get_maximum_velocity();
        double holeTolerance = course.get_hole_tolerance();
        // First calculate the direction the shot must have and set it's velocity to the maximum one
        Vector3d shotDirection = flagPosition.minus(ball_position).getNormalized();

        PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        sim.set_ball_position(ball_position);
        double stepSize = 10e-4;

        PriorityQueue<ShotData> data = new PriorityQueue<>(Comparator.comparingDouble(ShotData::getDistanceToTarget));

        ShotSimulationThread t1 = new ShotSimulationThread(ball_position, sim, shotDirection, stepSize, ballFlagDistance);
        t1.start();

        return new Vector3d();
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

    class ShotSimulationThread extends Thread {

        private Vector3d ball_position;
        private PuttingSimulator sim;
        private Vector3d shotDirection;

        private double stepSize;
        private double ballFlagDistance;
        private double maxVelocity;

        private ShotData bestShot;

        public ShotSimulationThread(Vector3d ball_position, PuttingSimulator sim, Vector3d shotDirection, double stepSize, double ballFlagDistance) {
            this.ball_position = ball_position;
            this.sim = sim;
            this.shotDirection = shotDirection;
            this.stepSize = stepSize;
            this.ballFlagDistance = ballFlagDistance;
            this.maxVelocity = sim.getCourse().get_maximum_velocity();
        }

        @Override
        public void run() {

            Vector3d shot = shotDirection.getScaled(maxVelocity);

            ShotData bestShot = null;
            double bestTargetDistance = Double.MAX_VALUE;

            double precision = 10e-3;
            double i = 1;

            double previousDistanceToTarget = Double.MAX_VALUE;
            double currentDistanceToTarget = 0;

            while (Math.abs(previousDistanceToTarget - currentDistanceToTarget) > precision) {

                ShotData outcomeCenter = simulateShot(ball_position, sim, shot, stepSize);
                double ballHoleDistFactor = ballFlagDistance / distanceBetween(ball_position, outcomeCenter.getCalculatedPosition());

                shot.scale(Math.pow(ballHoleDistFactor, 1/i));

                previousDistanceToTarget = currentDistanceToTarget;
                currentDistanceToTarget = outcomeCenter.getDistanceToTarget();

                if (currentDistanceToTarget < bestTargetDistance) {
                    bestShot = outcomeCenter;
                }

                i += 0.75;

            }

            if (bestShot.getShotVector().magnitude() >= maxVelocity) {
                Vector3d maxShot = shotDirection.getScaled(maxVelocity);
                bestShot = simulateShot(ball_position, sim, maxShot, stepSize);
            }
            this.bestShot = bestShot;

            System.out.println(this.bestShot);

        }

        public ShotData getBestShot() {
            return bestShot;
        }

    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("-0.01*x + 0.006*x^2 + 0.04 * y"), new Vector3d(0, 10));
        course.setMaximumVelocity(2);
        PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        PuttingBot bot = new HeuristicBot();

        Vector3d shot = bot.shot_velocity(course, sim.get_ball_position());

        //sim.take_shot(shot, 10e-4);
    }

}
