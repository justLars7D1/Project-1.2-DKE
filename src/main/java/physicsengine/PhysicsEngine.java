package physicsengine;

/**
 * A collective term of all physics engines
 */
public interface PhysicsEngine {
    void set_step_size(double step_size);
    void approximate();
    void setBallPosition(Vector3d ballPosition);
    void setBallVelocity(Vector3d ballVelocity);
    Vector3d getBallPosition();
    Vector3d getBallVelocity();
}