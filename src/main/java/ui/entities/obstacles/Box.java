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

public class Box extends Obstacle {

    private static TexturedModel model;
    private static final float widthX = (float) 2.190922, widthY = (float) 2.170072, widthZ = (float) 2.198802;

    static {
        Loader loader = new Loader();
        ModelData data = OBJLoader.loadOBJ("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\box\\box.obj");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\box\\box.png"));
        model = new TexturedModel(rawModel, texture);
    }

    public Box(Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
        Vector3f maxPosition = new Vector3f(this.getPosition().x() + (widthX/2), this.getPosition().y() + (widthY/2), this.getPosition().z() + (widthZ/2));
        Vector3f minPosition = new Vector3f(this.getPosition().x() - (widthX/2), this.getPosition().y() - (widthY/2), this.getPosition().z() - (widthZ/2));
        // Vector3f finalPosition = maxPosition - minPosition;
        return false;
    }
}