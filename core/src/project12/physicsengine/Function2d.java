package project12.physicsengine;

public interface Function2d {
    double evaluate(Vector2d p);
    Vector2d gradient(Vector2d p);
}
