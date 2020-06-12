package physicsengine;

import gameelements.PuttingCourse;

public class PhysicsLaws {

    PuttingCourse course;
    private static final Vector3d heightBasisVector = new Vector3d(0, 1, 0);

    public PhysicsLaws(PuttingCourse course) {
        this.course = course;
    }

    public Vector3d ballAcceleration(Vector3d ballPosition, Vector3d ballVelocity) {
        boolean isInAir = (ballPosition.get_y() > course.get_height().evaluate(ballPosition));
        Vector3d fieldGradient = course.get_height().gradient(ballPosition);
        Vector3d gravitationalForce = gravitationalForce(fieldGradient, isInAir);
        Vector3d totalAcceleration = gravitationalForce.copy();
        if (!isInAir) {
            Vector3d frictionalForce = frictionalForce(ballVelocity);
            totalAcceleration = totalAcceleration.add(frictionalForce);
        }
        totalAcceleration.scale(1/course.get_ball_mass());
        return totalAcceleration;
    }

    private Vector3d gravitationalForce(Vector3d fieldGradient, boolean isInAir) {
        double constantPart = -1 * course.get_ball_mass() * course.get_gravitational_constant();
        Vector3d forceVector;
        if (isInAir) {
            // No air resistance
            forceVector = new Vector3d(0, constantPart, 0);
        } else {
            Vector3d normalizedGradient = fieldGradient.getNormalized();
            Vector3d direction = heightBasisVector.minus(normalizedGradient.getScaled(heightBasisVector.dot(normalizedGradient)));

            Vector3d invertedNormal = normalizedGradient.getScaled(-1);
            double angleBetweenInvertedNormalAndGravity = heightBasisVector.calculateAngle(invertedNormal);

            forceVector = direction.getScaled(Math.sin(angleBetweenInvertedNormalAndGravity) * constantPart);
        }
        return forceVector;
    }

    private Vector3d frictionalForce(Vector3d ballVelocity) {
        double constantPart = -1 * course.get_friction_coefficient() * course.get_ball_mass() * course.get_gravitational_constant();
        Vector3d normalizedVelocity = ballVelocity.getNormalized();
        normalizedVelocity.scale(constantPart);
        return normalizedVelocity;
    }

}
