package ui.entities;

import org.joml.Vector3f;
import ui.models.TexturedModel;
import ui.renderEngine.Window;

public class UIPlayer extends Entity {

    private static final long WINDOW = Window.getWindow();

    public UIPlayer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Vector3f newPosition) {
        super.setPosition(newPosition);
    }

}
