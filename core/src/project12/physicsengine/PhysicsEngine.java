package project12.physicsengine;

/**
 * A collective term of all physics engines
 */
public interface PhysicsEngine {
    void setPositionVector(Vector2d v);
    void setVelocityVector(Vector2d v);
    void setAccelerationVector(Vector2d v);
    void approximate();
}