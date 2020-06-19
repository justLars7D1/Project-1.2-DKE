package ui.entities.obstacles;
import org.joml.Vector3f;
import org.lwjgl.odbc.SQL_YEAR_MONTH_STRUCT;
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
        ModelData data = OBJLoader.loadOBJ("game/entities/box/box");
        RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        ModelTexture texture = new ModelTexture(loader.loadTexture("game/entities/box/box"));
        model = new TexturedModel(rawModel, texture);
    }

    public Box(Vector3f position, float scale) {
        super(model, position, 0, 0, 0, scale);
    }

    public void resetMinimum() {
        setPosition(getPosition().add(0, widthY/2, 0));
    }

    @Override
    public boolean isHit(Vector3d ballPosition) {
        Vector3d maxPosition = new Vector3d(this.getPosition().x() + (widthX/2), this.getPosition().y() + (widthY/2), this.getPosition().z() + (widthZ/2));
        Vector3d minPosition = new Vector3d(this.getPosition().x() - (widthX/2), this.getPosition().y() - (widthY/2), this.getPosition().z() - (widthZ/2));

        System.out.println(ballPosition);

        if(ballPosition.get_x() >= minPosition.get_x() && ballPosition.get_x() <= maxPosition.get_x()){
            if( ballPosition.get_z() >= minPosition.get_z() && ballPosition.get_z() <= maxPosition.get_z()){
                return ballPosition.get_y() >= minPosition.get_y() && ballPosition.get_y() <= maxPosition.get_y();
            }
        }

        return false;
    }
}