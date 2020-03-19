package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import project12.physicsengine.Vector2d;
import project12.ui.Application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GameModeSelectionScreen extends AbstractScreen {

        private SpriteBatch batch;

        private Sprite backgroundImg;

        private ImageButton singlePlayerBtn;
        private ImageButton loadFromFileBtn;

        private volatile Vector2d[] moves;

        private boolean terminateLoop;
        @Override
        public void buildStage() {
            Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");
            batch = new SpriteBatch();

            backgroundImg = new Sprite(new Texture("gamemodeselection/mainbackground.png"));
            backgroundImg.setSize(getWidth(), getHeight());

            singlePlayerBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("gamemodeselection/SinglePlayerBtn.png"))));
            loadFromFileBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("gamemodeselection/FromFileBtn.png"))));

            singlePlayerBtn.setSize(4 / 5f * Application.screenSizeFactor * singlePlayerBtn.getWidth(), 4 / 5f * Application.screenSizeFactor * singlePlayerBtn.getHeight());
            loadFromFileBtn.setSize(4 / 5f * Application.screenSizeFactor * loadFromFileBtn.getWidth(), 4 / 5f * Application.screenSizeFactor * loadFromFileBtn.getHeight());

            addActor(singlePlayerBtn);
            addActor(loadFromFileBtn);

            //Single player gamemode
            singlePlayerBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    ScreenManager.getInstance().setScreen(ScreenEnum.GAME, ScreenManager.getInstance().getSimulation());
                    return true;
                }
            });


            //Loading shots from a file gamemode
            loadFromFileBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    terminateLoop = false;
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setDialogTitle("Load Shot Information!");
                            JFrame f = new JFrame();
                            f.setVisible(true);
                            f.toFront();
                            f.setVisible(false);
                            int res = chooser.showOpenDialog(f);
                            f.dispose();
                            if (res == JFileChooser.APPROVE_OPTION) {
                                File fileToLoad = chooser.getSelectedFile();
                                try {
                                    BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
                                    ArrayList<Vector2d> movesList = new ArrayList<>();
                                    String line = "";
                                    while ((line = reader.readLine()) != null) {
                                        String numbers = line.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\s", "");
                                        String[] values = numbers.split(",");
                                        movesList.add(new Vector2d(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
                                    }
                                    moves = movesList.toArray(new Vector2d[0]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                terminateLoop = true;
                            }
                        }
                    };
                    t.start();
                    while (moves == null && !terminateLoop) {
                        Thread.onSpinWait();
                    };
                    ScreenManager.getInstance().setScreen(ScreenEnum.GAME, ScreenManager.getInstance().getSimulation(), moves);
                    return true;
                }
            });
        }

        @Override
        public void render(float delta) {
            super.render(delta);
            Gdx.graphics.setTitle("Crazy Putting! - Gamemode Selection - " + Gdx.graphics.getFramesPerSecond() + "FPS");

            batch.begin();
            backgroundImg.draw(batch);
            singlePlayerBtn.draw(batch, 1.0f);
            loadFromFileBtn.draw(batch, 1.0f);
            singlePlayerBtn.setVisible(true);
            singlePlayerBtn.setPosition(550,550);
            loadFromFileBtn.setVisible(true);
            loadFromFileBtn.setPosition(550,400);
            batch.end();

        }

        @Override
        public void dispose() {
            super.dispose();
            batch.dispose();
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


