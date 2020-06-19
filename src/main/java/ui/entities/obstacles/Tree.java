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
        ModelData data = OBJLoader.loadOBJ("game/entities/trees/tree");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("game/entities/trees/tree"));
        model = new TexturedModel(rawModel, texture);
    }

    public Tree( Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
        Vector3d maxPosition = new Vector3d(this.getPosition().x() + (widthX/2), this.getPosition().y() + widthY, this.getPosition().z() + (widthZ/2));
        Vector3d minPosition = new Vector3d(this.getPosition().x() - (widthX/2), this.getPosition().y(), this.getPosition().z() - (widthZ/2));

        if(ballPosition.get_x() >= minPosition.get_x() && ballPosition.get_x() <= maxPosition.get_x()){
            if( ballPosition.get_z() >= minPosition.get_z() && ballPosition.get_z() <= maxPosition.get_z()){
                return ballPosition.get_y() >= minPosition.get_y() && ballPosition.get_y() <= maxPosition.get_y();
            }}

        return false;
    }
}
