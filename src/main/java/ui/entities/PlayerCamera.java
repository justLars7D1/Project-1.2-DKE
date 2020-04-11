package ui.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class PlayerCamera extends Camera {

    private UIPlayer UIPlayer;

    private float distanceFromPlayer = 20;
    private float angleAroundPlayer = 0;

    private float curX, curY;
    public PlayerCamera(UIPlayer UIPlayer, long window) {
        super(window);
        this.UIPlayer = UIPlayer;
        GLFW.glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override public void invoke (long win, double dx, double dy) {
                calculateZoom(dy);
            }
        });
        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                curX = (float) xpos;
                curY = (float) ypos;
            }
        });
    }

    public void switchPlayer(UIPlayer newUIPlayer) {
        this.UIPlayer = newUIPlayer;
    }

    private float calculateHDistance() {
        return (float) (this.distanceFromPlayer * Math.cos(Math.toRadians(this.pitch)));
    }

    private float calculateVDistance() {
        return (float) (this.distanceFromPlayer * Math.sin(Math.toRadians(this.pitch)));
    }

    float prevX;
    private float calcDX() {
        float dx = prevX - curX;
        prevX = curX;
        return dx;
    }

    float prevY;
    private float calcDY() {
        float dy = prevY - curY;
        prevY = curY;
        return dy;
    }

    public void move() {
        float dx = calcDX();
        float dy = calcDY();
        calculatePitch(dy);
        calculateAngleAroundPlayer(dx);
        float hDistance = calculateHDistance();
        float vDistance = calculateVDistance();
        calculateCameraPosition(hDistance, vDistance);
        this.yaw = 180 - (UIPlayer.getRotY() + this.angleAroundPlayer);
    }

    private void calculateCameraPosition(float hDistance, float vDistance) {
        float theta = UIPlayer.getRotY() + this.angleAroundPlayer;
        float xOffset = (float) (hDistance * Math.sin(Math.toRadians(theta)));
        float zOffset = (float) (hDistance * Math.cos(Math.toRadians(theta)));
        this.position.x = UIPlayer.getPosition().x - xOffset;
        this.position.z = UIPlayer.getPosition().z - zOffset;
        this.position.y = UIPlayer.getPosition().y + vDistance;
    }

    private void calculateZoom(double dy) {
        float zoomLevel = (float) (0.5f * dy);
        this.distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch(float dy) {
        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            float dPitch = dy * 0.1f;
            this.pitch += dPitch;
        }
    }

    private void calculateAngleAroundPlayer(float dx) {
        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            float dAngleChange = dx * 0.3f;
            this.angleAroundPlayer += dAngleChange;
        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public UIPlayer getUIPlayer() {
        return UIPlayer;
    }
}
