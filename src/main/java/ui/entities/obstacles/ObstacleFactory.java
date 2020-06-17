package ui.entities.obstacles;

import gameelements.PuttingCourse;
import org.joml.Vector3f;
import ui.entities.Obstacle;
import ui.objloader.OBJLoader;

public class ObstacleFactory {

    private static ObstacleFactory factory;
    private static PuttingCourse course;

    private ObstacleFactory() {
    }

    public static ObstacleFactory getFactory() {
        if (factory == null) {
            factory = new ObstacleFactory();
        }
        return factory;
    }

    public static void setCourse(PuttingCourse puttingCourse) {
        course = puttingCourse;
    }

    //use getObstacle method to get object of type obstacle
    public void createObstacleInCourse(String obstacleType, Vector3f position, float scale) {
        if (course != null) {
            Obstacle obstacle = null;

            if (obstacleType.equalsIgnoreCase("WALL")) {
                OBJLoader.loadOBJ(obstacleType);
                obstacle = new Wall(position, scale);
            } else if (obstacleType.equalsIgnoreCase("TREE")) {
                OBJLoader.loadOBJ(obstacleType);
                obstacle = new Tree(position, scale);
            } else if (obstacleType.equalsIgnoreCase("BOX")) {
                OBJLoader.loadOBJ(obstacleType);
                obstacle = new Box(position, scale);
            }
            course.addObstacle(obstacle);
        }
    }
}
