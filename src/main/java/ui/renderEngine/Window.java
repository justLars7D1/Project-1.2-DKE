package ui.renderEngine;

import lwjgui.LWJGUI;
import org.lwjgl.glfw.GLFW;

public class Window {

    private static final int WIDTH = (int)(1920*0.8), HEIGHT = (int)(1080*0.8);
    private static String title = "Crazy Putting!";
    private static long window;

    private static long lastFrameTime;
    private static float delta;

    private static long getCurrentTimeMilis() {
        return (long)(GLFW.glfwGetTime() * 1000);
    }

    public static void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("Couln't initialize GLFW");
            System.exit(-1);
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, title, 0, 0);

        if (window == 0) {
            System.err.println("Window couldn't be created");
            System.exit(-1);
        }

        GLFW.glfwSetWindowPos(window, WIDTH/8, HEIGHT/6);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);

        lastFrameTime = getCurrentTimeMilis();

    }

    public static boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public static void update() {
        GLFW.glfwPollEvents();
        long currentFrameTime = getCurrentTimeMilis();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static long getWindow() {
        return window;
    }

}
