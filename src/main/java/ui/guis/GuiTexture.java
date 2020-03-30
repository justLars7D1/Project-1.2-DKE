package ui.guis;

import org.joml.Vector2f;

public class GuiTexture {

    private int texture;
    private Vector2f position;
    private Vector2f scale;
    private int rotX, rotY, rotZ;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public int getRotX() {
        return rotX;
    }

    public void setRotX(int rotX) {
        this.rotX = rotX;
    }

    public int getRotY() {
        return rotY;
    }

    public void setRotY(int rotY) {
        this.rotY = rotY;
    }

    public int getRotZ() {
        return rotZ;
    }

    public void setRotZ(int rotZ) {
        this.rotZ = rotZ;
    }
}
