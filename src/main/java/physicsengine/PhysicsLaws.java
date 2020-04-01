package physicsengine;

import gameelements.PuttingCourse;

public class PhysicsLaws {

    PuttingCourse course;

    public PhysicsLaws(PuttingCourse course) {
        this.course = course;
    }

    public Vector3d ballAcceleration(Vector3d ballPosition, Vector3d ballVelocity) {
        Vector3d fieldGradient = course.get_height().gradient(ballPosition);
        Vector3d gravitationalForce = gravitationalForce(fieldGradient);
        Vector3d frictionalForce = frictionalForce(ballVelocity);
        Vector3d totalAcceleration = gravitationalForce.add(frictionalForce);
        totalAcceleration.scale(1/course.get_ball_mass());
        return totalAcceleration;
    }

    private Vector3d gravitationalForce(Vector3d fieldGradient) {
        double constantPart = -1 * course.get_ball_mass() * course.get_gravitational_constant();
        return new Vector3d(constantPart * fieldGradient.get_x(), constantPart * fieldGradient.get_z());
    }

    private Vector3d frictionalForce(Vector3d ballVelocity) {
        double constantPart = -1 * course.get_friction_coefficient() * course.get_ball_mass() * course.get_gravitational_constant();
        Vector3d normalizedVelocity = ballVelocity.getNormalized();
        normalizedVelocity.scale(constantPart);
        return normalizedVelocity;
    }

}
