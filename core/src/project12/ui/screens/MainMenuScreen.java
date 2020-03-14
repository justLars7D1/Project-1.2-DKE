package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import project12.ui.Application;

class MainMenuScreen extends AbstractScreen {

    private final SpriteBatch batch;

    private final Sprite backgroundImage;
    private final ImageButton startGameBtn;
    private final ImageButton courseDesignerBtn;
    private final ImageButton exitBtn;

    public MainMenuScreen() {
        batch = new SpriteBatch();

        backgroundImage = new Sprite(new Texture("mainmenu/BackgroundImg.png"));

        startGameBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("mainmenu/StartGameBtn.png"))));
        courseDesignerBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("mainmenu/CourseDesignerBtn.png"))));
        exitBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("mainmenu/ExitBtn.png"))));
    }

    @Override
    public void buildStage() {

        Gdx.graphics.setTitle("Crazy Putting! - Main Menu - " + Gdx.graphics.getFramesPerSecond() + "FPS");

        backgroundImage.setSize(getWidth(), getHeight());

        float sizeFactor = (float) (0.4f*Math.sqrt(Application.screenSizeFactor));
        startGameBtn.setSize(sizeFactor*getWidth(), sizeFactor*getHeight());
        courseDesignerBtn.setSize(sizeFactor*getWidth(), sizeFactor*getHeight());
        exitBtn.setSize(sizeFactor*getWidth(), sizeFactor*getHeight());

        sizeFactor = 0.7f;
        startGameBtn.setPosition(getWidth()/2 - startGameBtn.getWidth()/2, sizeFactor*getHeight() - startGameBtn.getHeight()/2);
        courseDesignerBtn.setPosition(getWidth()/2 - courseDesignerBtn.getWidth()/2, (sizeFactor*2/3f)*getHeight() - courseDesignerBtn.getHeight()/2);
        exitBtn.setPosition(getWidth()/2 - exitBtn.getWidth()/2, (sizeFactor*1/3f)*getHeight() - exitBtn.getHeight()/2);

        addActor(startGameBtn);
        addActor(courseDesignerBtn);
        addActor(exitBtn);

        startGameBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().setScreen(ScreenEnum.GAME);
                return false;
            }
        });

        courseDesignerBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().setScreen(ScreenEnum.DESIGNER);
                return false;
            }
        });

        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return false;
            }
        });

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.graphics.setTitle("Crazy Putting! - Main Menu - " + Gdx.graphics.getFramesPerSecond() + "FPS");

        batch.begin();
        backgroundImage.draw(batch);
        startGameBtn.draw(batch, 1.0f);
        courseDesignerBtn.draw(batch, 1.0f);
        exitBtn.draw(batch, 1.0f);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

}
