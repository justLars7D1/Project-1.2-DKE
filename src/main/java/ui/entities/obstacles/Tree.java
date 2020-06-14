package ui.entities.obstacles;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.entities.Entity;
import ui.entities.Obstacle;
import ui.models.RawModel;
import ui.models.TexturedModel;
import ui.objloader.ModelData;
import ui.objloader.OBJLoader;
import ui.renderEngine.Loader;
import ui.textures.ModelTexture;

public class Tree extends Obstacle {

    public static TexturedModel model;
    private static final float widthX = (float) 1.292204, widthY = (float) 2.435676, widthZ = (float) 1.292204;

    static {
        Loader loader = new Loader();
        ModelData data = OBJLoader.loadOBJ("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\trees\\tree.obj");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\trees\\tree.png"));
        model = new TexturedModel(rawModel, texture);
    }

    public Tree( Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
            return false;
    }
}
