package project12.ui.screens;

import com.badlogic.gdx.Gdx;
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

        backgroundImage.setSize(Application.screenSizeFactor * backgroundImage.getWidth(), Application.screenSizeFactor * backgroundImage.getHeight());

        startGameBtn.setSize(0.6f*startGameBtn.getWidth(), 0.6f*startGameBtn.getHeight());
        courseDesignerBtn.setSize(0.6f*courseDesignerBtn.getWidth(), 0.6f*courseDesignerBtn.getHeight());
        exitBtn.setSize(0.6f*exitBtn.getWidth(), 0.6f*exitBtn.getHeight());

        startGameBtn.setPosition(getWidth()/2 - startGameBtn.getWidth()/2, 0.6f*getHeight());
        courseDesignerBtn.setPosition(getWidth()/2 - courseDesignerBtn.getWidth()/2, 0.4f*getHeight());
        exitBtn.setPosition(getWidth()/2 - exitBtn.getWidth()/2, 0.2f*getHeight());

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
