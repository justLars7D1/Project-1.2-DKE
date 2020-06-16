package ui.renderEngine;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import physicsengine.functions.Function2d;
import ui.entities.*;
import ui.models.RawModel;
import ui.models.TexturedModel;
import ui.objloader.ModelData;
import ui.objloader.OBJLoader;
import ui.terrains.FunctionalHeightsGenerator;
import ui.terrains.MasterTerrain;
import ui.terrains.Terrain;
import ui.textures.ModelTexture;
import ui.water.WaterFrameBuffers;
import ui.water.WaterRenderer;
import ui.water.WaterShader;
import ui.water.WaterTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMap {

    private FunctionalHeightsGenerator heightsGenerator;

    private final Loader loader;

    private final WaterFrameBuffers fbos;
    private final WaterShader waterShader;

    private MasterTerrain terrain;
    private List<Entity> allEntities;
    private List<WaterTile> waterTiles;

    public GameMap(int minx, int minY, int maxX, int maxY, Function2d heightFunction, Loader loader) {
        this.heightsGenerator = new FunctionalHeightsGenerator(heightFunction);
        this.loader = loader;
        this.allEntities = new ArrayList<>();
        this.terrain = new MasterTerrain(minx, minY, maxX, maxY, heightsGenerator, loader);

        this.fbos = new WaterFrameBuffers();
        this.waterShader = new WaterShader();
        this.waterTiles = new ArrayList<>();
        waterTiles.add(new WaterTile(0, 0, 0));

    }

    public void addPlayers(Entity ... players) {
        this.allEntities.addAll(Arrays.asList(players));
    }

    public WaterShader getWaterShader() {
        return waterShader;
    }

    public WaterFrameBuffers getFbos() {
        return fbos;
    }

    public FunctionalHeightsGenerator getHeightsGenerator() {
        return this.heightsGenerator;
    }

    public void addObstacles(List<Obstacle> entities) {
        allEntities.addAll(entities);
    }

    public List<Entity> getAllEntities() {
        return allEntities;
    }

    public List<WaterTile> getWaterTiles() {
        return waterTiles;
    }

    public List<Terrain> getTerrain() {
        return terrain.getTerrains();
    }

    public void generateEntityAtPosition(String objFileName, String textureFileName, float x, float z, float reflectivity, float shineDamper, float scale, int numTextures) {

        ModelData data = OBJLoader.loadOBJ(objFileName);
        RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());

        ModelTexture texture = new ModelTexture(loader.loadTexture(textureFileName));
        texture.setReflectivity(reflectivity);
        texture.setShineDamper(shineDamper);

        TexturedModel texturedModel = new TexturedModel(model, texture);

        Vector3f position = new Vector3f(x, 0, z);
        position.y = terrain.getHeightOfTerrain(position.x, position.z);

        Entity entity = new Entity(texturedModel, position, 0,0,0,scale, numTextures);
        allEntities.add(entity);

    }

    public float getWaterLevel() {
        return waterTiles.get(0).getHeight();
    }

    public void renderMap(MasterRenderer renderer, WaterRenderer waterRenderer, Camera camera, List<Light> lights) {
        GL30.glEnable(GL30.GL_CLIP_DISTANCE0);

        //Render reflection texture
        getFbos().bindReflectionFrameBuffer();
        float distance = 2 * (camera.getPosition().y - getWaterLevel());
        camera.getPosition().y -= distance;
        camera.invertPitch();
        renderer.renderScene(getAllEntities(), getTerrain(), lights, camera, new Vector4f(0, 1, 0, -getWaterLevel()));
        camera.getPosition().y += distance;
        camera.invertPitch();

        //Render refraction texture
        getFbos().bindRefractionFrameBuffer();
        renderer.renderScene(getAllEntities(), getTerrain(), lights, camera, new Vector4f(0, -1, 0, getWaterLevel()));

        //Render to screen
        GL30.glDisable(GL30.GL_CLIP_DISTANCE0);
        getFbos().unbindCurrentFrameBuffer();
        renderer.renderScene(getAllEntities(), getTerrain(), lights, camera, new Vector4f(0, -1, 0, 10000000));
        waterRenderer.render(getWaterTiles(), camera);
    }

    public MasterTerrain getMasterTerrain() {
        return this.terrain;
    }

    public void cleanUp() {
        fbos.cleanUp();
    }

    public void addEntity(Entity entity) {
        if (!allEntities.contains(entity)) {
            this.allEntities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        this.allEntities.remove(entity);
    }

}
