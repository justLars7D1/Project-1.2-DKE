package ui.renderEngine;

import ui.entities.Camera;
import ui.entities.PlayerCamera;
import ui.entities.Entity;
import ui.entities.Light;
import ui.models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import ui.shaders.StaticShader;
import ui.shaders.TerrainShader;
import ui.skybox.SkyboxRenderer;
import ui.terrains.Terrain;
import ui.toolbox.Maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private SkyboxRenderer skyboxRenderer;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    //{R, G, B}
    private static final float[] skyColor = {0.5444f, 0.62f, 0.69f};

    private Matrix4f projectionMatrix;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        this.skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public void prepare() {
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(skyColor[0], skyColor[1], skyColor[2], 1);
    }

    public static void enableCulling() {
        GL30.glEnable(GL30.GL_CULL_FACE);
        GL30.glCullFace(GL30.GL_BACK);
    }

    public static void disableCulling() {
        GL30.glDisable(GL30.GL_CULL_FACE);
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
        for(Terrain terrain: terrains)
            processTerrain(terrain);

        for (Entity entity: entities)
            processEntity(entity);

        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        prepare();

        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColor(skyColor[0], skyColor[1], skyColor[1]);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(skyColor[0], skyColor[1], skyColor[2]);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        skyboxRenderer.render(camera, skyColor[0], skyColor[1], skyColor[2]);

        entities.clear();
        terrains.clear();
    }

    private void createProjectionMatrix() {
        projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }


    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getModel();
        if (!entities.containsKey(texturedModel)) {
            entities.put(texturedModel, new ArrayList<>());
        }
        entities.get(texturedModel).add(entity);
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
