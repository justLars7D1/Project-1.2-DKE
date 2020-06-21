package ui.entities.obstacles;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.entities.Obstacle;
import ui.models.TexturedModel;

public class Lamppost extends Obstacle {

    private static final float widthX = (float) 0, widthY = (float) 0, widthZ = (float) 0;
    private static TexturedModel model;

    public Lamppost(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
        return false;
    }
}
