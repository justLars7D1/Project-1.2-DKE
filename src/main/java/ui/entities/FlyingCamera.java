package ui.entities;

import org.lwjgl.glfw.GLFW;
import ui.renderEngine.Window;

public class FlyingCamera extends Camera {

    public FlyingCamera(long window) {
        super(window);
        position.y = 3;
        pitch = 0;
    }

    public void move() {
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
            position.z -= 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS){
            position.z += 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            position.x += 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS){
            position.x -= 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS){
            position.y += 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS){
            position.y -= 0.2f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS){
            yaw += 1f;
        }
        if(GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS){
            yaw -= 1f;
        }
    }

}
