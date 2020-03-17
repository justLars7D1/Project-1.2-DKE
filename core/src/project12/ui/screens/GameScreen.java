package project12.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import project12.gameelements.PuttingCourse;
import project12.gameelements.PuttingSimulator;
import project12.physicsengine.Vector2d;
import project12.ui.FunctionRenderer;

class GameScreen extends AbstractScreen {

    private PuttingSimulator simulator;
    private PuttingCourse course;

    private ModelBatch batch;
    private ModelInstance golfBall;
    private final float radius;

    private FunctionRenderer terrainRenderer;
    private PerspectiveCamera camera;
    private CameraInputController controller;
    private Environment environment;

    public GameScreen(Object ... param) {
        this.batch = new ModelBatch();
        this.simulator = (PuttingSimulator)(param[0]);
        this.course = this.simulator.getCourse();
        this.terrainRenderer = new FunctionRenderer(this.course);
        //1184 is the golf ball's density
        this.radius = (float) Math.cbrt((3*course.get_ball_mass())/(4*Math.PI*1184));

        camera = new PerspectiveCamera(120, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector2d initialBallPosition = this.simulator.get_ball_position();
        camera.position.set((float)(initialBallPosition.get_x()) + 4f, (float)(this.course.get_height().evaluate(initialBallPosition)) + 1f, (float)(initialBallPosition.get_y()) + 4f);
        camera.lookAt((float)(initialBallPosition.get_x()), (float)(this.course.get_height().evaluate(initialBallPosition)) + radius/2, (float)(initialBallPosition.get_y()));
        camera.near = 0.01f;
        camera.far = 3000.0f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -1.0f, 1f));

        ModelBuilder modelBuilder = new ModelBuilder();
        Model golfBall = modelBuilder.createSphere(radius*2,radius*2,radius*2,10,10,
                new Material(ColorAttribute.createDiffuse(Color.RED)),VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelBuilder.begin();
        this.golfBall = new ModelInstance(golfBall, (float)(initialBallPosition.get_x()), (float)(this.course.get_height().evaluate(initialBallPosition)) + radius/2, (float)(initialBallPosition.get_y()));
        modelBuilder.end();

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
        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        terrainRenderer.dispose();
    }

}
