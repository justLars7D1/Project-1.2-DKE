package ui.entities.obstacles;
import org.joml.Vector3f;
import ui.entities.Entity;
import ui.entities.Obstacle;
import ui.models.TexturedModel;

public class Box extends Entity implements Obstacle {


    public Box(TexturedModel model, Vector3f position,float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    @Override
    public boolean isHit() {
        return false;
    }
}