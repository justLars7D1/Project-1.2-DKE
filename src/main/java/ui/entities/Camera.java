package ui.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public abstract class Camera {

    protected final long window;

    protected Vector3f position = new Vector3f(0,0,0);
    protected float pitch = 20;
    protected float yaw;
    protected float roll;

    protected float curX, curY;
    protected Camera(long window) {
        this.window = window;
        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                curX = (float) xpos;
                curY = (float) ypos;
            }
        });
    }

    public abstract void move();

    public void invertPitch() {
        this.pitch = -this.pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getCurX() {
        return curX;
    }

    public float getCurY() {
        return curY;
    }

}
