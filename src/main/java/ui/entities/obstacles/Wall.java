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

public class Wall extends Obstacle {

    public static TexturedModel model;
    private static final float widthX = (float) 0, widthY = (float) 0, widthZ = (float) 0;

    public Wall(Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    static {
        Loader loader = new Loader();
        ModelData data = OBJLoader.loadOBJ("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\wall\\wall.obj");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("D:\\Projects\\Project-1.2-DKE\\res\\game\\entities\\wall\\wall.jpg"));
        model = new TexturedModel(rawModel, texture);
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
        /* if(ballPosition.get_x()>= minWallpostion.get_x() && ballPosition.get_x()<= maxWallpostion.get_x())
              if( ballPosition.get_z()>= minWallpostion.get_z() && ballPosition.get_z()<= maxWallpostion.get_z())
                return true;
         */
        return false;
    }

}
