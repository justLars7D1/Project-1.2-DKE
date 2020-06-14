package ui.entities;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.models.TexturedModel;

import java.util.List;

public abstract class Obstacle extends Entity {

    private List<Obstacle> listOfObstacles;

    public Obstacle(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    protected abstract boolean isHit(Vector3d ballPosition);

    public void setListOfObstacles(Obstacle obs) {
        listOfObstacles.add(obs);
    }
}
