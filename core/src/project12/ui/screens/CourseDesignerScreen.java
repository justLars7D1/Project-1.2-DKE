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

class CourseDesignerScreen extends AbstractScreen {

    private PuttingSimulator simulator;

    private SpriteBatch batch;

    private static final Skin uiSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));

    private Sprite backgroundImg;
    private ImageButton saveChangesBtn;
    private ImageButton exportToFileBtn;
    private ImageButton loadFromFileBtn;

    private TextField functionField;
    private TextField startPointField;
    private TextField holePointField;
    private TextField holeToleranceField;
    private TextField ballMassField;
    private TextField gravitationField;
    private TextField frictionField;
    private TextField maximumVelocityField;

    @Override
    public void buildStage() {
        Gdx.graphics.setTitle("Crazy Putting! - Designer - " + Gdx.graphics.getFramesPerSecond() + "FPS");
        batch = new SpriteBatch();

        backgroundImg = new Sprite(new Texture("coursedesigner/BackgroundImg.png"));
        backgroundImg.setSize(getWidth(), getHeight());

        saveChangesBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("coursedesigner/SaveChangesBtn.png"))));
        exportToFileBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("coursedesigner/exportToFileBtn.png"))));
        loadFromFileBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("coursedesigner/LoadFromFileBtn.png"))));

        saveChangesBtn.setSize(4/5f*Application.screenSizeFactor*saveChangesBtn.getWidth(), 4/5f*Application.screenSizeFactor*saveChangesBtn.getHeight());
        exportToFileBtn.setSize(4/5f*Application.screenSizeFactor*exportToFileBtn.getWidth(), 4/5f*Application.screenSizeFactor*exportToFileBtn.getHeight());
        loadFromFileBtn.setSize(4/5f*Application.screenSizeFactor*loadFromFileBtn.getWidth(),4/5f*Application.screenSizeFactor*loadFromFileBtn.getHeight());

        saveChangesBtn.setPosition(0.4f*getWidth() - saveChangesBtn.getWidth(), 0.22f*getHeight() - saveChangesBtn.getHeight());
        exportToFileBtn.setPosition(0.42f*getWidth() - exportToFileBtn.getWidth() + saveChangesBtn.getWidth(), 0.22f*getHeight() - exportToFileBtn.getHeight());
        loadFromFileBtn.setPosition(0.44f*getWidth() - loadFromFileBtn.getWidth() + saveChangesBtn.getWidth() + exportToFileBtn.getWidth(), 0.22f*getHeight() - exportToFileBtn.getHeight());

        simulator = ScreenManager.getInstance().getSimulation();
        if (simulator == null) {
            functionField = new TextField("-0.01*x + 0.003*x^2 + 0.04 * y", uiSkin);
            startPointField = new TextField("(0.0, 0.0)", uiSkin);
            holePointField = new TextField("(0.0, 10.0)", uiSkin);
            holeToleranceField = new TextField("0.02", uiSkin);
            ballMassField= new TextField("45.93", uiSkin);
            gravitationField = new TextField("9.81", uiSkin);
            frictionField = new TextField("0.131", uiSkin);
            maximumVelocityField = new TextField("3", uiSkin);
        } else {
            PuttingCourse course = simulator.getCourse();
            functionField = new TextField(course.get_height().toString(), uiSkin);
            startPointField = new TextField(course.get_start_position().toString(), uiSkin);
            holePointField = new TextField(course.get_flag_position().toString(), uiSkin);
            holeToleranceField = new TextField(String.valueOf(course.get_hole_tolerance()), uiSkin);
            ballMassField= new TextField(String.valueOf(course.get_ball_mass()), uiSkin);
            gravitationField = new TextField(String.valueOf(course.get_gravitational_constant()), uiSkin);
            frictionField = new TextField(String.valueOf(course.get_friction_coefficient()), uiSkin);
            maximumVelocityField = new TextField(String.valueOf(course.get_maximum_velocity()), uiSkin);
        }

        initTextField(functionField, 0.0f);
        initTextField(startPointField, 0.065f);
        initTextField(holePointField, 0.13f);
        initTextField(holeToleranceField, 0.195f);
        initTextField(ballMassField, 0.26f);
        initTextField(gravitationField, 0.325f);
        initTextField(frictionField, 0.39f);
        initTextField(maximumVelocityField, 0.455f);

        addActor(saveChangesBtn);
        addActor(exportToFileBtn);
        addActor(loadFromFileBtn);

        //Add a listener for exporting to files
        exportToFileBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            PuttingSimulator s = createSimulation();
            new Thread(() -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Save Course!");
                JFrame f = new JFrame();
                f.setVisible(true);
                f.toFront();
                f.setVisible(false);
                int res = chooser.showSaveDialog(f);
                f.dispose();
                if (res == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = chooser.getSelectedFile();
                    s.saveCourse(fileToSave.getAbsolutePath());
                }
            }).start();
            return true;
            }
        });

        //Add a listener for loading to files
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
                        setCourseText(course);
                    } else {
                        System.out.println("Error... Could not load in the specified course!");
                    }
                }
            }).start();
            return true;
            }
        });

        //Add a listener for saving the changes
        saveChangesBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PuttingSimulator s = createSimulation();
                ScreenManager.getInstance().setSimulation(s);
                ScreenManager.getInstance().setScreen(ScreenEnum.MAIN);
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
        saveChangesBtn.draw(batch, 1.0f);
        exportToFileBtn.draw(batch, 1.0f);
        loadFromFileBtn.draw(batch, 1.0f);

        functionField.draw(batch, 1.0f);
        startPointField.draw(batch, 1.0f);
        holePointField.draw(batch, 1.0f);
        holeToleranceField.draw(batch, 1.0f);
        ballMassField.draw(batch, 1.0f);
        gravitationField.draw(batch, 1.0f);
        frictionField.draw(batch, 1.0f);
        maximumVelocityField.draw(batch, 1.0f);
        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    /**
     * Method for initializing a text field (correct size and position on screen)
     * @param field The textfield
     * @param yOffset The offset of Y from the first textfield
     */
    private void initTextField(TextField field, float yOffset) {
        field.setSize(0.2f*getWidth(), 0.05f*getHeight());
        field.setPosition(0.52f*getWidth(), (0.765f - yOffset)*getHeight() - field.getHeight());
        addActor(field);
    }

    /**
     * Parse a vector string to a vector
     * @param text The string representing a vector
     * @return The vector
     */
    private Vector2d fieldToVec2d(String text) {
        text = text.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");
        String[] split = text.split(",");
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        return new Vector2d(x, y);
    }

    private void setCourseText(PuttingCourse course) {
        functionField.setText(course.get_height().toString());
        startPointField.setText(course.get_start_position().toString());
        holePointField.setText(course.get_flag_position().toString());
        holeToleranceField.setText(String.valueOf(course.get_hole_tolerance()));
        ballMassField.setText(String.valueOf(course.get_ball_mass()));
        gravitationField.setText(String.valueOf(course.get_gravitational_constant()));
        frictionField.setText(String.valueOf(course.get_friction_coefficient()));
        maximumVelocityField.setText(String.valueOf(course.get_maximum_velocity()));
    }

    /**
     * Creates the simulation object and adds all properties to it
     * @return THe simulation object
     */
    private PuttingSimulator createSimulation() {
        Function2d courseFunction = new FunctionParserRPN(functionField.getText());
        Vector2d startPoint = fieldToVec2d(startPointField.getText());
        Vector2d holePoint = fieldToVec2d(holePointField.getText());

        PuttingCourse course = new PuttingCourse(courseFunction, startPoint, holePoint);
        course.setHoleTolerance(Double.parseDouble(holeToleranceField.getText()));
        course.setBallMass(Double.parseDouble(ballMassField.getText()));
        course.setGravitationalConstant(Double.parseDouble(gravitationField.getText()));
        course.set_friction_coefficient(Double.parseDouble(frictionField.getText()));
        course.setMaximumVelocity(Double.parseDouble(maximumVelocityField.getText()));

        PhysicsEngine engine = new VerletSolver();
        return new PuttingSimulator(course, engine);
    }

}
