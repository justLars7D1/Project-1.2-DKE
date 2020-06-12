package ui.entities.obstacles;

import org.joml.Vector3f;
import ui.entities.Obstacle;
import ui.models.TexturedModel;
import ui.objloader.OBJLoader;

public class ObstacleFactory {

    //use getObstacle method to get object of type obstacle
    public Obstacle getObstacle(String obstacleType,TexturedModel model, Vector3f position, float scale){
        if(obstacleType == null){
            return null;
        }
        if(obstacleType.equalsIgnoreCase("WALL")){
            OBJLoader.loadOBJ(obstacleType);
            return new Wall( position,  scale);

        } else if(obstacleType.equalsIgnoreCase("TREE")){
            OBJLoader.loadOBJ(obstacleType);
            return new Tree(position,  scale);

        } else if(obstacleType.equalsIgnoreCase("BOX")){
            OBJLoader.loadOBJ(obstacleType);
            return new Box( position,  scale);
        }
        return null;
    }
}
