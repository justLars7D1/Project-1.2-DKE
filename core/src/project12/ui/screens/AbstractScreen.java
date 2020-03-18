package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import project12.ui.Application;

import static com.badlogic.gdx.Gdx.input;

/**
 * Represents a screen in the UI
 */
abstract class AbstractScreen extends Stage implements Screen {

    /**
     * Constructor with keyboard shortcut for toggling fullscreen
     */
    protected AbstractScreen() {
        super(new ScreenViewport());

        addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.F11) {
                    boolean fullScreen = Gdx.graphics.isFullscreen();
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                    if (fullScreen)
                        Gdx.graphics.setWindowedMode((int)(Application.screenSizeFactor*1920), (int)(Application.screenSizeFactor*1080));
                    else
                        Gdx.graphics.setFullscreenMode(currentMode);
                }
                return true;
            }
        });

    }

    /**
     * Called upon generating the screen
     */
    public abstract void buildStage();

    /**
     * Render method
     * @param delta the delta time
     */
    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {
        input.setInputProcessor(this);
    }

    @Override public void resize(int width, int height) {
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

}
