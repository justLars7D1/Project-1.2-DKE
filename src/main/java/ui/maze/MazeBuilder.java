package ui.maze;


import dijkstra.Graph;
import org.joml.Vector3f;
import physicsengine.Vector3d;
import physicsengine.functions.Function2d;
import ui.entities.Obstacle;
import ui.entities.obstacles.ObstacleFactory;

import java.util.List;

public class MazeBuilder {

    private static final ObstacleFactory OBSTACLE_FACTORY =  ObstacleFactory.getFactory() ;

    public static void buildMaze(Graph maze, List<Obstacle> obstacleList, Function2d function2d){

        for (int i = 0; i < maze.nodeCount(); i++) {
            Vector3d v = (Vector3d) maze.getValue(i); // coordinate of vertex

            for (int neighbor: maze.neighbors(i)) {
                Vector3d n = (Vector3d) maze.getValue(neighbor);
                Vector3d currentNodeToNeighbor = n.minus(v); // subtract the coord of neighbor from the vertex
                double length = currentNodeToNeighbor.magnitude(); // gets distance between vertex and its neighbor
                currentNodeToNeighbor.normalize(); // coord becomes 1 1 1

                for (double j = length/2.190922; j < length; j += length/2.190922) {
                    Vector3d firstPoint = currentNodeToNeighbor.getScaled(j);
                    Vector3d rotateLeft = firstPoint.getRotatedYAxis(90).getNormalized().getScaled(2.190922);
                    Vector3d boxPositionLeft = v.add(firstPoint).add(rotateLeft);
                    Vector3d boxPositionRight = v.add(firstPoint).minus(rotateLeft);
                    Vector3f uiPositionLeft = new Vector3f((float)boxPositionLeft.get_x(), (float)boxPositionLeft.get_y(), (float)boxPositionLeft.get_z());
                    Vector3f uiPositionRight = new Vector3f((float)boxPositionRight.get_x(), (float)boxPositionRight.get_y(), (float)boxPositionRight.get_z());
                    Obstacle obstacleL = OBSTACLE_FACTORY.createObstacle("box", uiPositionLeft, 1);
                    Obstacle obstacleR = OBSTACLE_FACTORY.createObstacle("box", uiPositionRight, 1);
                    obstacleList.add(obstacleL);
                    obstacleList.add(obstacleR);
                }

            }

        }

    }

    public static void main(String[] args) {

    }
}
