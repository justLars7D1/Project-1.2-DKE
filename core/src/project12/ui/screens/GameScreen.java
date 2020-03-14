package project12.ui.screens;

import com.badlogic.gdx.Gdx;

class GameScreen extends AbstractScreen {

    @Override
    public void buildStage() {
        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
