package project12.ui.screens;

import com.badlogic.gdx.Gdx;

class CourseDesignerScreen extends AbstractScreen {

    @Override
    public void buildStage() {
        Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
