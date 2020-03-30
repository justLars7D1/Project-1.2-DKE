package physicsengine;

import gameelements.PuttingCourse;

public class PhysicsLaws {

    PuttingCourse course;

    public PhysicsLaws(PuttingCourse course) {
        this.course = course;
    }

    public Vector2d ballAcceleration(Vector2d ballPosition, Vector2d ballVelocity) {
        Vector2d fieldGradient = course.get_height().gradient(ballPosition);
        Vector2d gravitationalForce = gravitationalForce(fieldGradient);
        Vector2d frictionalForce = frictionalForce(ballVelocity);
        Vector2d totalAcceleration = gravitationalForce.add(frictionalForce);
        totalAcceleration.scale(1/course.get_ball_mass());
        return totalAcceleration;
    }

    private Vector2d gravitationalForce(Vector2d fieldGradient) {
        double constantPart = -1 * course.get_ball_mass() * course.get_gravitational_constant();
        return new Vector2d(constantPart * fieldGradient.get_x(), constantPart * fieldGradient.get_y());
    }

    private Vector2d frictionalForce(Vector2d ballVelocity) {
        double constantPart = -1 * course.get_friction_coefficient() * course.get_ball_mass() * course.get_gravitational_constant();
        Vector2d normalizedVelocity = ballVelocity.getNormalized();
        normalizedVelocity.scale(constantPart);
        return normalizedVelocity;
    }

}
