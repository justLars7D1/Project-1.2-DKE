package project12.ui.screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import project12.gameelements.PuttingCourse;
import project12.gameelements.PuttingSimulator;
import project12.physicsengine.Vector2d;
import project12.physicsengine.engines.VerletSolver;
import project12.ui.FunctionRenderer;

import javax.swing.*;
import java.io.File;

class GameScreen extends AbstractScreen {

    private PuttingSimulator simulator;
    private PuttingCourse course;

    private ModelBatch batch;
    private ModelInstance golfBall;
    private ModelInstance flag;
    private final float radius;

    private FunctionRenderer terrainRenderer;
    private PerspectiveCamera camera;
    private CameraInputController controller;
    private Environment environment;

    private SpriteBatch spriteBatch;
    private BitmapFont font = new BitmapFont();
    private double shotVelocity = 0.05;

    public GameScreen(Object ... param) {
        this.batch = new ModelBatch();
        this.spriteBatch = new SpriteBatch();
        this.simulator = (PuttingSimulator)(param[0]);
        this.course = this.simulator.getCourse();
        this.terrainRenderer = new FunctionRenderer(this.course);
        //1184 is the golf ball's density
        this.radius = (float) Math.cbrt((3*course.get_ball_mass())/(4*Math.PI*1184));

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector2d initialBallPosition = this.simulator.get_ball_position();
        camera.position.set((float)(initialBallPosition.get_x()) + 7f, 2*(float)(this.course.get_height().evaluate(initialBallPosition)) + 3f, (float)(initialBallPosition.get_y()) + 7f);
        camera.lookAt((float)(initialBallPosition.get_x()), 2*(float)(this.course.get_height().evaluate(initialBallPosition)), (float)(initialBallPosition.get_y()));
        camera.near = 0.01f;
        camera.far = 3000.0f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -1.0f, 1f));

        ModelBuilder modelBuilder = new ModelBuilder();
        Model golfBall = modelBuilder.createSphere(radius*2,radius*2,radius*2,10,10,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelBuilder.begin();
        this.golfBall = new ModelInstance(golfBall, (float)(initialBallPosition.get_x()), 2*(float)(this.course.get_height().evaluate(initialBallPosition)) + radius, (float)(initialBallPosition.get_y()));
        modelBuilder.end();

        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle("game/flag.g3dj", Files.FileType.Internal));
        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        Vector2d flagPosition = course.get_flag_position();
        flag = new ModelInstance(model, (float) flagPosition.get_x() + 2f, 2*(float) course.get_height().evaluate(flagPosition), (float) flagPosition.get_y());

        controller = new CameraInputController(camera);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, controller));

    }

    @Override
    public void buildStage() {
        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(new InputMultiplexer(this, controller));
        controller.update();
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.setTitle("Crazy Putting! " + Gdx.graphics.getFramesPerSecond() + "FPS");

        camera.update();

        terrainRenderer.render(camera, environment);
        batch.begin(camera);
        batch.render(golfBall, environment);
        batch.render(flag, environment);
        batch.end();

        spriteBatch.begin();
        font.draw(spriteBatch, String.format("Shot velocity: %.2f m/s", shotVelocity), 0.85f*getWidth(), 0.9f*getHeight());
        font.draw(spriteBatch, "Press \"SPACE\" to take the shot", 0.85f*getWidth(), 0.85f*getHeight());
        spriteBatch.end();

        if (isUpUp) {
            double velToAdd = 0.05;
            if (shotVelocity+velToAdd <= course.get_maximum_velocity()) {
                shotVelocity += velToAdd;
            } else {
                shotVelocity = course.get_maximum_velocity();
            }
        }
        if (isDownUp) {
            double velToAdd = -0.05;
            if (shotVelocity+velToAdd >= 0.05) {
                shotVelocity += velToAdd;
            } else {
                shotVelocity = 0;
            }
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        terrainRenderer.dispose();
    }

    private boolean isDownUp = false;
    private boolean isUpUp = false;
    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.SPACE) {
            Thread t = new Thread(() -> {
                Vector2d shot = new Vector2d(camera.direction.x, camera.direction.z);
                shot.normalize();
                shot.scale(shotVelocity);
                simulator.take_shot(shot, golfBall, radius);
            });
            t.start();
            System.out.println(course.holeReached(simulator.get_ball_position()));
        }
        if (keyCode == Input.Keys.UP)
            isUpUp = true;
        if (keyCode == Input.Keys.DOWN)
            isDownUp = true;
        return true;
    }

    @Override
    public boolean keyUp(int keyCode) {
        if (keyCode == Input.Keys.UP)
            isUpUp = false;
        if (keyCode == Input.Keys.DOWN)
            isDownUp = false;
        return true;
    }
}
