package ui.entities;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.models.TexturedModel;

import java.util.List;

public abstract class Obstacle extends Entity {

    public Obstacle(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public abstract boolean isHit(Vector3d ballPosition);

}
