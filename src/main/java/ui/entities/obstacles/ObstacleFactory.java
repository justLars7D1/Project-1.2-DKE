package ui.entities.obstacles;

import org.joml.Vector3f;
import ui.entities.Obstacle;
import ui.models.TexturedModel;

public class ObstacleFactory {

    //use getShape method to get object of type shape
    public Obstacle getObstacle(String obstacleType,TexturedModel model, Vector3f position, float scale){
        if(obstacleType == null){
            return null;
        }
        if(obstacleType.equalsIgnoreCase("WALL")){
            return new Wall( model,  position,  scale);

        } else if(obstacleType.equalsIgnoreCase("TREE")){
            return new Tree( model,  position,  scale);

        } else if(obstacleType.equalsIgnoreCase("BOX")){
            return new Box( model,  position,  scale);
        }
        return null;
    }
}