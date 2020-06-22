package ui;

import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;
import gameelements.PuttingCourse;
import gameelements.PuttingSimulator;
import lwjgui.LWJGUI;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.Slider;
import lwjgui.scene.control.TextField;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.floating.FloatingPane;
import lwjgui.style.BackgroundNVGImage;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import physicsengine.PhysicsEngine;
import physicsengine.Vector3d;
import physicsengine.engines.EulerSolver;
import physicsengine.engines.RK3;
import physicsengine.engines.RK4;
import physicsengine.engines.RK5;
import physicsengine.engines.VerletSolver;
import physicsengine.functions.Function2d;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.Obstacle;
import ui.entities.obstacles.Box;
import ui.entities.obstacles.Lamppost;
import ui.entities.obstacles.ObstacleFactory;
import ui.maze.MazeBuilder;
import ui.maze.MazeLoader;
import ui.renderEngine.Window;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseDesignerScreen {

    private static final ObstacleFactory OBSTACLE_FACTORY = ObstacleFactory.getFactory();
    private List<Obstacle> allObstacles = new ArrayList<>();
    private static final String[] OBSTACLES = {"Tree", "Box","Lamppost"};
    private int obstacleCounter = 0;
    private TextField positionInField = new TextField("(0.0, 0.0)");

    private File graphFile;

    private Label obstacleLabel = new Label(OBSTACLES[0]);

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
        setupObstacleInput(floatingPane);
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

        this.courseSettingsBtns.get("load maze").setOnMouseClicked(e -> {
            new Thread(() -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Load Maze!");
                JFrame f = new JFrame();
                f.setVisible(true);
                f.toFront();
                f.setVisible(false);
                int res = chooser.showOpenDialog(f);
                f.dispose();
                if (res == JFileChooser.APPROVE_OPTION) {
                    graphFile = chooser.getSelectedFile();
                }
            }).start();
        });




        pane.setBackground(new BackgroundNVGImage(backgroundImage.getImage()));

        /*if (worked) {
                        PuttingCourse course = s.getCourse();
                        Graph maze= MazeLoader.loadMaze("src/main/java/dijkstra/maze-on-course", course );
                        MazeBuilder.buildMaze(maze);*/
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

    private void setupObstacleInput(FloatingPane pane) {
        FloatingPane labelPane = new FloatingPane();
        FloatingPane sliderPane = new FloatingPane();
        FloatingPane textfieldPane = new FloatingPane();
        FloatingPane buttonPane = new FloatingPane();

        labelPane.setAbsolutePosition(0.85*Window.getWidth(), 0.4*Window.getHeight());
        sliderPane.setAbsolutePosition(0.8*Window.getWidth(), 0.45*Window.getHeight());
        textfieldPane.setAbsolutePosition(0.8*Window.getWidth(), 0.5*Window.getHeight());
        buttonPane.setAbsolutePosition(0.8*Window.getWidth(), 0.55*Window.getHeight());

        Slider obstacleSlider = new Slider();
        obstacleSlider.setMin(0);
        obstacleSlider.setMax(10);
        obstacleSlider.setValue(0);
        obstacleSlider.setOnValueChangedEvent((event -> {
            if (obstacleCounter + 1 >= OBSTACLES.length) obstacleCounter = 0;
            else obstacleCounter++;
            obstacleLabel.setText(OBSTACLES[obstacleCounter]);
        }));
        obstacleSlider.setBlockIncrement(10);

        positionInField.setPrefSize(200, 30);
        positionInField.setFontSize(22);
        courseSettingsFields.put("obstacles", positionInField);

        Button obstacleBtn = new Button("Add Obstacle!");
        obstacleBtn.setPrefSize(200, 30);
        obstacleBtn.setFontSize(22);
        obstacleBtn.setOnMouseClicked(e -> addObstacleToCourse());

        obstacleSlider.setPrefSize(200, 30);
        obstacleLabel.setFontSize(30);

        labelPane.getChildren().add(obstacleLabel);
        sliderPane.getChildren().add(obstacleSlider);
        textfieldPane.getChildren().add(positionInField);
        buttonPane.getChildren().add(obstacleBtn);

        pane.getChildren().addAll(labelPane, sliderPane, textfieldPane, buttonPane);
        //obstacleSlider.setAbsolutePosition(900, 500);
    }

    private void addObstacleToCourse() {
        Vector3d position = fieldToVec2d(positionInField.getText());
        Vector3f fieldPosition = new Vector3f((float)position.get_x(), (float)position.get_y(), (float)position.get_z());
        Obstacle obstacle = OBSTACLE_FACTORY.createObstacle(obstacleLabel.getText(), fieldPosition, 1);
        allObstacles.add(obstacle);
        positionInField.setText("(0.0, 0.0)");
    }

    private void setupButtons(FloatingPane floatingPane) {
        String[] buttonNames = {"continue", "save", "load", "load maze"};
        String[] initialValues = {"Continue", "Save to file", "Load from file", "load maze from file"};
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

        if (graphFile != null) {
            GraphAL mazeGraph = (GraphAL) ReadFile.setCoordinates(graphFile.getAbsolutePath(), course);
            course.setMaze(mazeGraph);
            MazeBuilder.buildMaze(mazeGraph, allObstacles, courseFunction);
        }

        for (Obstacle o: allObstacles) {
            Vector3f position = o.getPosition();
            double heightOnMap = courseFunction.evaluate(position.x, position.z);
            Vector3f newHeight = new Vector3f(position.x, (float) heightOnMap, position.z);
            o.setPosition(newHeight);
            if (o instanceof Box) ((Box) o).resetMinimum();
            System.out.println("P: " + position);
        }
        course.addObstacles(allObstacles);
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
            case "rk3":
                engine = new RK3(course);
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
