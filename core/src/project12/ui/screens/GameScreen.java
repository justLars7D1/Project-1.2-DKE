package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

class GameScreen extends AbstractScreen {

    @Override
    public void buildStage() {
        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear the screen with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
