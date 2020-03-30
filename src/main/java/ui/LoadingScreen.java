package ui;

import ui.guis.GuiRenderer;
import ui.guis.GuiTexture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;
import ui.renderEngine.Loader;
import ui.renderEngine.Texture;
import ui.renderEngine.Window;

import java.util.ArrayList;
import java.util.List;

public class LoadingScreen {

    private static final int LOADING_TEXTURE = new Texture("./res/loading/mainLoadingImg.png").getTextureID();
    private static final int ROTATION_ACCELERATION = 0;

    private boolean loadingFinished;
    private final GuiRenderer guiRenderer;
    private final List<GuiTexture> guis = new ArrayList<>();

    private int rotationDegree = -180;
    private int rotationSpeed = (int) (360 / 2.9);
    private final GuiTexture loadingIcon = new GuiTexture(LOADING_TEXTURE, new Vector2f(0f, 0f), new Vector2f(.7f, .7f));

    public LoadingScreen(Loader loader) {
        this.guiRenderer = new GuiRenderer(loader);
        guis.add(loadingIcon);
    }

    public void update() {
        finish();
        guiRenderer.render(guis);
        rotationDegree += rotationSpeed * Window.getFrameTimeSeconds();
        //rotationSpeed += ROTATION_ACCELERATION * Window.getFrameTimeSeconds();
        loadingIcon.setRotZ(rotationDegree);
        if (rotationDegree >= 360) {
            loadingFinished = true;
        }
    }

    public void finish() {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(0, 0, 0, 1);
    }

    public boolean isLoadingFinished() {
        return loadingFinished;
    }

    public void cleanUp() {
        guiRenderer.cleanUp();
    }

}
