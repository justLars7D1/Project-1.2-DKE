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
            Vector3d v = (Vector3d) maze.getValue(i);
            //System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());

            for (int neighbour: maze.neighbors(i)){
                Vector3d n= (Vector3d) maze.getValue(neighbour);

                for (int x= (int) v.get_x()+1; x<n.get_x()+1; x++) {
                    for (int z =(int) v.get_z()-1; z<n.get_z()-1; z++) {
                        Vector3d currentPosX = (Vector3d) maze.getValue(x);
                        Vector3d currentPosZ = (Vector3d) maze.getValue(z);
                        Vector3f boxPos = new Vector3f((float) currentPosX.get_x(),
                                (float) function2d.evaluate(currentPosX.get_x(), currentPosZ.get_z()),
                                (float) currentPosZ.get_z());

                        Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
                        obstacleList.add(obstacle);
                    }
                }
                for (int x= (int) v.get_x()-1; x<n.get_x()-1; x++) {
                    for (int z =(int) v.get_z()+1; z<n.get_z()+1; z++) {
                        Vector3d currentPosX = (Vector3d) maze.getValue(x);
                        Vector3d currentPosZ = (Vector3d) maze.getValue(z);
                        Vector3f boxPos = new Vector3f((float) currentPosX.get_x(),
                                (float) function2d.evaluate(currentPosX.get_x(), currentPosZ.get_z()),
                                (float) currentPosZ.get_z());

                        Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
                        obstacleList.add(obstacle);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {

    }
}
