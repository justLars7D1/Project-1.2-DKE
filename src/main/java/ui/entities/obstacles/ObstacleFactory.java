package ui.entities.obstacles;

import org.joml.Vector3f;
import ui.entities.Obstacle;
import ui.objloader.OBJLoader;

public class ObstacleFactory {

    private static ObstacleFactory factory;

    private ObstacleFactory() {
    }

    public static ObstacleFactory getFactory() {
        if (factory == null) {
            factory = new ObstacleFactory();
        }
        return factory;
    }

    //use getObstacle method to get object of type obstacle
    public Obstacle createObstacle(String obstacleType, Vector3f position, float scale) {
        Obstacle obstacle = null;

        if (obstacleType.equalsIgnoreCase("WALL")) {
            obstacle = new Wall(position, scale);
        } else if (obstacleType.equalsIgnoreCase("TREE")) {
            obstacle = new Tree(position, scale);
        } else if (obstacleType.equalsIgnoreCase("BOX")) {
            obstacle = new Box(position, scale);
        }

        return obstacle;
    }
}
