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
        Vector3d maxPosition = new Vector3d(this.getPosition().x() + (widthX/2), this.getPosition().y() + (widthY/2), this.getPosition().z() + (widthZ/2));
        Vector3d minPosition = new Vector3d(this.getPosition().x() - (widthX/2), this.getPosition().y() - (widthY/2), this.getPosition().z() - (widthZ/2));

        if(ballPosition.get_x()>= minPosition.get_x() && ballPosition.get_x()<= maxPosition.get_x()){
            if( ballPosition.get_z()>= minPosition.get_z() && ballPosition.get_z()<= maxPosition.get_z()){
                if( ballPosition.get_y()>= minPosition.get_y() && ballPosition.get_y()<= maxPosition.get_y()){
                    return true;}}}

        return false;
    }

}
