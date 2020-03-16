package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import project12.gameelements.PuttingCourse;
import project12.gameelements.PuttingSimulator;
import project12.physicsengine.PhysicsEngine;
import project12.physicsengine.Vector2d;
import project12.physicsengine.engines.VerletSolver;
import project12.physicsengine.functions.Function2d;
import project12.physicsengine.functions.FunctionParserRPN;
import project12.ui.Application;

import javax.swing.*;
import java.io.File;

public class GameModeSelectionScreen extends AbstractScreen {

        private PuttingSimulator simulator;

        private SpriteBatch batch;

        private static final Skin uiSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        private Sprite backgroundImg;

        private ImageButton startGameBtn;
        private ImageButton loadFromFileBtn;

        @Override
        public void buildStage() {
            Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");
            batch = new SpriteBatch();

            backgroundImg = new Sprite(new Texture("gamemodeselection/mainbackground.png"));
            backgroundImg.setSize(getWidth(), getHeight());

            startGameBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("gamemodeselection/SinglePlayerBtn.png"))));
            loadFromFileBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("gamemodeselection/FromFileBtn.png"))));

            startGameBtn.setSize(4 / 5f * Application.screenSizeFactor * startGameBtn.getWidth(), 4 / 5f * Application.screenSizeFactor * startGameBtn.getHeight());
            loadFromFileBtn.setSize(4 / 5f * Application.screenSizeFactor * loadFromFileBtn.getWidth(), 4 / 5f * Application.screenSizeFactor * loadFromFileBtn.getHeight());


            simulator = ScreenManager.getInstance().getSimulation();
            addActor(startGameBtn);
            addActor(loadFromFileBtn);

            startGameBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    new Thread(() -> {
                        JFrame f = new JFrame();
                        f.setVisible(true);
                        f.toFront();
                        f.setVisible(false);

                    }).start();
                    return true;
                }
            });


            loadFromFileBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    new Thread(() -> {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setDialogTitle("Load Course!");
                        JFrame f = new JFrame();
                        f.setVisible(true);
                        f.toFront();
                        f.setVisible(false);
                        int res = chooser.showOpenDialog(f);
                        f.dispose();
                        if (res == JFileChooser.APPROVE_OPTION) {
                            File fileToLoad = chooser.getSelectedFile();
                            PuttingSimulator s = new PuttingSimulator(new VerletSolver());
                            boolean worked = s.loadCourse(fileToLoad.getAbsolutePath());
                            //If the file is successfully loaded in, set the simulation
                            if (worked) {
                                PuttingCourse course = s.getCourse();
                            } else {
                                System.out.println("Error... Could not load in the specified course!");
                            }
                        }
                    }).start();
                    return true;
                }
            });

        }

        @Override
        public void render(float delta) {
            super.render(delta);
            Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");

            batch.begin();
            backgroundImg.draw(batch);
            startGameBtn.draw(batch, 1.0f);
            loadFromFileBtn.draw(batch, 1.0f);
            startGameBtn.setVisible(true);
            startGameBtn.setPosition(550,550);
            loadFromFileBtn.setVisible(true);
            loadFromFileBtn.setPosition(550,400);
            batch.end();

        }

        @Override
        public void dispose() {
            super.dispose();
        }

        private void initTextField(TextField field, float yOffset) {
            field.setSize(0.2f*getWidth(), 0.05f*getHeight());
            field.setPosition(0.52f*getWidth(), (0.765f - yOffset)*getHeight() - field.getHeight());
            addActor(field);
        }

        private Vector2d fieldToVec2d(String text) {
            text = text.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");
            String[] split = text.split(",");
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            return new Vector2d(x, y);
        }

}


