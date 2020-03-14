package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class LoadingScreen extends AbstractScreen {

    private final SpriteBatch batch;

    private final Texture loadingTexture;
    private final Sprite loadingSprite;

    private float alpha = 0.2f;
    private float rotation = 0f;
    private final float totalDuration = 4f;

    public LoadingScreen() {
        this.batch = new SpriteBatch();
        this.loadingTexture = new Texture(Gdx.files.internal("loading/mainLoadingImg.png"));
        this.loadingSprite =  new Sprite(this.loadingTexture);
    }

    @Override
    public void buildStage() {

        Gdx.graphics.setTitle("Crazy Putting! - Loading - " + Gdx.graphics.getFramesPerSecond() + "FPS");

        loadingSprite.setSize(0.6f * loadingSprite.getWidth(), 0.6f * loadingSprite.getHeight());
        loadingSprite.setPosition(getWidth()/2 - loadingSprite.getWidth()/2, getHeight()/2 - loadingSprite.getHeight()/2);
        loadingSprite.setAlpha(alpha);
        loadingSprite.setOriginCenter();

    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle("Crazy Putting! - Loading - " + Gdx.graphics.getFramesPerSecond() + "FPS");

        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear the screen with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        if (Gdx.graphics.getFramesPerSecond() != 0 && alpha < 1.2f) {
            alpha += (1f / Gdx.graphics.getFramesPerSecond()) / totalDuration;
            rotation += 360 * ((1f / Gdx.graphics.getFramesPerSecond()) / totalDuration*1.5);
            if (alpha <= 1) loadingSprite.setAlpha(alpha);
            if (rotation < 360f) loadingSprite.setRotation(rotation);
        } else if (Gdx.graphics.getFramesPerSecond() != 0) {
            ScreenManager.getInstance().setScreen(ScreenEnum.MAIN);
        }

        batch.begin();
        loadingSprite.draw(batch);
        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        loadingTexture.dispose();
        batch.dispose();
    }

}
