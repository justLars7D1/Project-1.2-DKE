package ui.entities.obstacles;

import org.joml.Vector3f;
import physicsengine.Vector3d;
import ui.entities.Obstacle;
import ui.models.RawModel;
import ui.models.TexturedModel;
import ui.objloader.ModelData;
import ui.objloader.OBJLoader;
import ui.renderEngine.Loader;
import ui.textures.ModelTexture;

@Deprecated
public class Lamppost extends Obstacle {

    private static final float widthX = (float) 1.934912, widthY = (float) 8.85824, widthZ = (float) 1.933775;
    private static TexturedModel model;

    static {
        Loader loader = new Loader();
        ModelData data = OBJLoader.loadOBJ("game/entities/lamppost/lamppost");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("game/entities/lamppost/lamppost"));
        model = new TexturedModel(rawModel, texture);
    }

    public Lamppost(Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    private static final double error = 10e-2;

    @Override
    public boolean isHit(Vector3d ballPosition) {
        Vector3d maxPosition = new Vector3d(this.getPosition().x() + (widthX / 2), this.getPosition().y() + (widthY / 2), this.getPosition().z() + (widthZ / 2));
        Vector3d minPosition = new Vector3d(this.getPosition().x() - (widthX / 2), this.getPosition().y() - (widthY / 2), this.getPosition().z() - (widthZ / 2));

        if(ballPosition.get_x() >= minPosition.get_x()-error && ballPosition.get_x() <= maxPosition.get_x()+error){
            if( ballPosition.get_z() >= minPosition.get_z()-error && ballPosition.get_z() <= maxPosition.get_z()+error){
                if( ballPosition.get_y() >= minPosition.get_y()-error && ballPosition.get_y() <= maxPosition.get_y()+error){
                    return true;
                }
            }
        }
        return false;
    }
}
