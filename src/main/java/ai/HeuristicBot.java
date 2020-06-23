package ai;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import physicsengine.Vector;
import physicsengine.Vector3d;
import physicsengine.engines.EulerSolver;
import physicsengine.engines.RK4;
import physicsengine.engines.RK5;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.Obstacle;

import java.sql.Time;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HeuristicBot implements PuttingBot {

    /**
     * Calculates the velocity needed for a shot from the data it receives
     * @param course The game course
     * @param ball_position The position of the ball
     * @return The calculated velocity for an optimal shot
     */
    @Override
    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position) {
        Vector3d flagPosition = course.get_flag_position();
        Vector3d ballPosition =  ball_position;
        double ballFlagDistance = distanceBetween(ball_position, flagPosition);

        double maxVelocity = course.get_maximum_velocity();

        // First calculate the direction the shot must have and set it's velocity to the maximum one
        Vector3d shotDirection = flagPosition.minus(ball_position).getNormalized();

        // We create the simulator to take shots with
        // PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        PuttingSimulator sim = new PuttingSimulator(course, new RK5(course));
        sim.set_ball_position(ball_position);
        double stepSize = 0.01;

        // Get the direction of the shot (pointing directly at the flag)
        Vector3d shot = shotDirection.getScaled(maxVelocity);

        // Store the best shot in here
        ShotData bestShot = null;
        double bestTargetDistance = Double.MAX_VALUE;

        // Scaling factor for scaling the shot
        double scalingFactorShot = 1;

        // The angle of initial rotation when the ball hits water
        double angle = 10;
        int angleMultiplcationFactor = 2;
        double iteration = 0;

        // Run the loop for 7 seconds and while it's not in a hole
        long startTime = System.nanoTime();
        long timeLimit = 7;
        boolean isInHole = false;
        while ((System.nanoTime() - startTime)/10e8 <= timeLimit && !isInHole) {

            // Simulate the shot
            ShotData resultingPosition = simulateShot(ball_position, sim, shot, stepSize);

            //if (!sim.isInWater()) {
            for(Obstacle obs : course.getObstacles()) {
                if (obs.isHit(ballPosition)) {

                    // Set the angle to rotate if it hits an obstacle back to it's original value divided by two (to not change too much)
                    angle = 5;
                    iteration = 0;

                    // Calculate the proportion of the start position and the flag to the ball position and the flag
                    double ballHoleDistFactor = ballFlagDistance / distanceBetween(ball_position, resultingPosition.getCalculatedPosition());

                    // Then we calculate the angle between the start and flag and the ball and flag
                    Vector3d startResultVector = resultingPosition.getCalculatedPosition().minus(ball_position);
                    Vector3d startFlagVector = flagPosition.minus(ball_position);
                    double ballHoleAngle = 180 / Math.PI * Math.acos(
                            (startResultVector.dot(startFlagVector)) / (startResultVector.magnitude() * startFlagVector.magnitude())
                    );

                    // Calculate if the ball is on the left or the right of the flag using the cross product
                    Vector3d crossProductStartFlagAndStartSimulated = startFlagVector.cross(resultingPosition.getCalculatedPosition());

                    // We scale the shot by the factor we calculated
                    shot.scale(Math.pow(ballHoleDistFactor, 0.95 / scalingFactorShot));

                    // We rotate the shot, compensating for the old "final" position
                    shot.rotateYAxis(((crossProductStartFlagAndStartSimulated.get_y() > 0) ? -1 : 1) * (0.95 / scalingFactorShot) * ballHoleAngle);

                    // If it ends up with a better result, we store it
                    if (resultingPosition.getDistanceToTarget() < bestTargetDistance) {
                        bestShot = resultingPosition;
                    }

                    isInHole = sim.isInHole();

                    scalingFactorShot += 0.5;

                } else {

                    shot.rotateYAxis(Math.pow(-1, iteration) * angle);
                    if (iteration++ % 2 == 1) {
                        angle *= angleMultiplcationFactor;
                    }

                    System.out.println(shot);

                    if (bestShot == null || bestShot.getDistanceToTarget() > resultingPosition.getDistanceToTarget()) {
                        bestShot = resultingPosition;
                    }

                }

            }

        }

        if (bestShot.getShotVector().magnitude() >= maxVelocity) {
            bestShot.getShotVector().normalize();
            bestShot.getShotVector().scale(maxVelocity);
        }

        return bestShot.getShotVector();
    }

    @Override
    public Vector3d shot_velocity(PuttingCourse course, Vector3d ball_position, Vector3d goal_position) {
        return new Vector3d();
    }

    /**
     * Simulate a shot in the game
     * @param ballPos The positon of the ball
     * @param simulator The simulator
     * @param shot The velocity of the shot
     * @param stepSize The engine's step size
     * @return The data of the resulting shot
     */
    private ShotData simulateShot(Vector3d ballPos, PuttingSimulator simulator, Vector3d shot, double stepSize) {
        simulator.set_ball_position(ballPos);
        simulator.take_shot(shot, stepSize);
        Vector3d newPos = simulator.get_ball_position();
        return new ShotData(shot, distanceBetween(newPos, simulator.getCourse().get_flag_position()), newPos);
    }

    /**
     * Calculate euclidean distance between two vectors
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return The euclidean distance
     */
    private double distanceBetween(Vector3d v1, Vector3d v2) {
        return Math.sqrt(Math.pow(v1.get_x() - v2.get_x(), 2)
                + Math.pow(v1.get_z() - v2.get_z(), 2));
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("-0.01*x + 0.003*x^2 + 0.04 * y"), new Vector3d(0, 10));
        course.setMaximumVelocity(10);
        // PuttingSimulator sim = new PuttingSimulator(course, new RK4(course));
        PuttingSimulator sim = new PuttingSimulator(course, new RK5(course));
        PuttingBot bot = new HeuristicBot();

        Vector3d shot = bot.shot_velocity(course, sim.get_ball_position());

        sim.take_shot(shot, 0.01);
        System.out.println(sim.get_ball_position());
    }

}