package physicsengine;

/**
 * A collective term of all physics engines
 */
public interface PhysicsEngine {
    void set_step_size(double step_size);
    void approximate();
    void setBallPosition(Vector2d ballPosition);
    void setBallVelocity(Vector2d ballVelocity);
    Vector2d getBallPosition();
    Vector2d getBallVelocity();
}