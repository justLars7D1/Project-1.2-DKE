package ui;

import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import lwjgui.LWJGUI;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.TextField;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.floating.FloatingPane;
import lwjgui.style.BackgroundNVGImage;
import org.lwjgl.opengl.GL30;
import physicsengine.PhysicsEngine;
import physicsengine.Vector3d;
import physicsengine.engines.EulerSolver;
import physicsengine.engines.RK4;
import physicsengine.engines.RK5;
import physicsengine.engines.VerletSolver;
import physicsengine.functions.Function2d;
import physicsengine.functions.FunctionParserRPN;
import ui.renderEngine.Window;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class CourseDesignerScreen {

    private static final BackgroundImg backgroundImage = new BackgroundImg("./res/coursedesigner/BackgroundImg.png");
    private final StackPane pane;

    private HashMap<String, TextField> courseSettingsFields;
    private HashMap<String, Button> courseSettingsBtns;

    private boolean doneSelecting;

    public CourseDesignerScreen() {
        this.courseSettingsFields = new HashMap<>();
        this.courseSettingsBtns = new HashMap<>();
        this.pane = new StackPane();
    }

    /**
     * Creates the screen
     * @param guiWindow The window to put the screen on
     */
    public void create(lwjgui.scene.Window guiWindow) {
        guiWindow.getScene().setRoot(pane);

        FloatingPane floatingPane = new FloatingPane();
        this.pane.getChildren().add(floatingPane);
        floatingPane.setAbsolutePosition(Window.getWidth()/2f - floatingPane.getWidth()/2, 0.245 * Window.getHeight());

        setupTextFields(floatingPane);
        setupButtons(floatingPane);

        this.courseSettingsBtns.get("continue").setOnMouseClicked(e -> doneSelecting = true);

        this.courseSettingsBtns.get("save").setOnMouseClicked(e -> {
            PuttingSimulator s = createSimulator("euler");
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
        });

        this.courseSettingsBtns.get("load").setOnMouseClicked(e -> {
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
                    PuttingSimulator s = new PuttingSimulator();
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
        });

        pane.setBackground(new BackgroundNVGImage(backgroundImage.getImage()));

    }

    public void update() {
        finish();
        LWJGUI.render();
    }

    public void finish() {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(0, 0, 0, 1);
    }

    public boolean isDoneSelecting() {
        return doneSelecting;
    }

    private void setupButtons(FloatingPane floatingPane) {
        String[] buttonNames = {"continue", "save", "load"};
        String[] initialValues = {"Continue", "Save to file", "Load from file"};
        for (int i = 0; i < buttonNames.length; i++) {
            FloatingPane t = new FloatingPane();
            t.setAbsolutePosition(0.25 * Window.getWidth() + 0.175 * i * Window.getWidth(), 0.8 * Window.getHeight());
            floatingPane.getChildren().add(t);

            Button btn = new Button(initialValues[i]);
            courseSettingsBtns.put(buttonNames[i], btn);
            btn.setPrefSize(200, 100);
            btn.setFontSize(22);
            t.getChildren().add(btn);
        }
    }

    /**
     * Creates the simulation object and adds all properties to it
     * @return THe simulation object
     */
    public PuttingSimulator createSimulator(String solverType) {
        Function2d courseFunction = new FunctionParserRPN(getContent("height_function"));
        Vector3d startPoint = fieldToVec2d(getContent("starting_point"));
        Vector3d holePoint = fieldToVec2d(getContent("target_point"));

        PuttingCourse course = new PuttingCourse(courseFunction, startPoint, holePoint);
        course.setHoleTolerance(Double.parseDouble(getContent("goal_tolerance")));
        course.setBallMass(Double.parseDouble(getContent("ball_mass")));
        course.setGravitationalConstant(Double.parseDouble(getContent("gravity")));
        course.setMaximumVelocity(Double.parseDouble(getContent("maximum_velocity")));

        PhysicsEngine engine;
        switch(solverType.toLowerCase()) {
            case "euler":
                engine = new EulerSolver(course);
                break;
            case "verlet":
                engine = new VerletSolver(course);
                break;
            case "rk5":
                engine = new RK5(course);
                break;
            case "rk4":
            default:
                engine = new RK4(course);
                break;
        }

        return new PuttingSimulator(course, engine);
    }

    private String getContent(String id) {
        return courseSettingsFields.get(id).getText();
    }

    /**
     * Parse a vector string to a vector
     * @param text The string representing a vector
     * @return The vector
     */
    private Vector3d fieldToVec2d(String text) {
        text = text.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");
        String[] split = text.split(",");
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        return new Vector3d(x, y);
    }

    private void setupTextFields(FloatingPane floatingPane) {
        String[] textFieldNames = {"height_function", "starting_point", "target_point", "goal_tolerance", "ball_mass",
        "gravity", "friction", "maximum_velocity"};
        String[] initialValues = {"-0.01*x + 0.003*x^2 + 0.04 * y", "(0.0, 0.0)", "(0.0, 10.0)", "0.02", "45.93",
        "9.81", "0.131", "3"};
        for (int i = 0; i < textFieldNames.length; i++) {
            FloatingPane t = new FloatingPane();
            t.setAbsolutePosition(floatingPane.getX()+16, floatingPane.getY() + 0.065 * i * Window.getHeight());
            floatingPane.getChildren().add(t);

             // Put a label in the floating pane
    TextField tmpField = new TextField(initialValues[i]);
            courseSettingsFields.put(textFieldNames[i], tmpField);
                    tmpField.setFontSize(22);
                    tmpField.setPrefSize(400, 20);
                    t.getChildren().add(tmpField);
            }
    }

private void setCourseText(PuttingCourse course) {
        courseSettingsFields.get("height_function").setText(course.get_height().toString());
        courseSettingsFields.get("starting_point").setText(course.get_start_position().toString());
        courseSettingsFields.get("target_point").setText(course.get_flag_position().toString());
        courseSettingsFields.get("goal_tolerance").setText(String.valueOf(course.get_hole_tolerance()));
        courseSettingsFields.get("ball_mass").setText(String.valueOf(course.get_ball_mass()));
        courseSettingsFields.get("gravity").setText(String.valueOf(course.get_gravitational_constant()));
        courseSettingsFields.get("friction").setText(String.valueOf(course.get_friction_coefficient()));
        courseSettingsFields.get("maximum_velocity").setText(String.valueOf(course.get_maximum_velocity()));
        }

public void setDoneSelecting(boolean doneSelecting) {
        this.doneSelecting = doneSelecting;
        }
}
