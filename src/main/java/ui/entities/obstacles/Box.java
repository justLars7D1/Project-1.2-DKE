package ui.entities.obstacles;
import org.joml.Vector3f;
import ui.entities.Entity;
import ui.entities.Obstacle;
import ui.models.TexturedModel;

public class Box extends Obstacle {

    public static TexturedModel model;

    public Box(Vector3f position,float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    @Override
    public boolean isHit() {

        return false;
    }
}