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

        /*for (int i = 0; i < maze.nodeCount(); i++) {
            Vector3d v = (Vector3d) maze.getValue(i);
            int index = graph.getvalue();

            if(index<100) {
                Vector3f boxPos = new Vector3f((float) v.get_x() + 2, (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z());

            } else if(index<200) {
                Vector3f boxPos = new Vector3f((float) v.get_x(), (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z()+2);

            } else {
                Vector3f boxPos = new Vector3f((float) v.get_x(), (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z()+2);
            }

            System.out.println(v);
            Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
            obstacleList.add(obstacle);

        }

        for (int i = 0; i < maze.nodeCount(); i++) {
            Vector3d v = (Vector3d) maze.getValue(i);
            int index = GraphAL.getvalue();

            if(index<100) {
                Vector3f boxPos = new Vector3f((float) v.get_x() - 2, (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z());

            } else if(index<200) {
                Vector3f boxPos = new Vector3f((float) v.get_x(), (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z()-2);

            } else {
                Vector3f boxPos = new Vector3f((float) v.get_x()+2, (float) function2d.evaluate(v.get_x(), v.get_z()), (float) v.get_z());
            }

            System.out.println(v);
            Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
            obstacleList.add(obstacle);

        }*/

        for (int i = 0; i < maze.nodeCount(); i++) {
            Vector3d v = (Vector3d) maze.getValue(i);

            for (int neighbor: maze.neighbors(i)) {
                Vector3d n = (Vector3d) maze.getValue(neighbor);
                Vector3d currentNodeToNeighbor = n.minus(v);
                double length = currentNodeToNeighbor.magnitude();
                currentNodeToNeighbor.normalize();

                for (double j = length/2.190922; j < length; j += length/2.190922) {
                    Vector3d firstPoint = currentNodeToNeighbor.getScaled(j);
                    Vector3d rotateLeft = firstPoint.getRotatedYAxis(90).getNormalized().getScaled(2.190922);
                    Vector3d boxPostitionLeft = v.add(firstPoint).add(rotateLeft);
                    Vector3d boxPostitionRight = v.add(firstPoint).minus(rotateLeft);
                    Vector3f uiPositionLeft = new Vector3f((float)boxPostitionLeft.get_x(), (float)boxPostitionLeft.get_y(), (float)boxPostitionLeft.get_z());
                    Vector3f uiPositionRight = new Vector3f((float)boxPostitionRight.get_x(), (float)boxPostitionRight.get_y(), (float)boxPostitionRight.get_z());
                    Obstacle obstacleL = OBSTACLE_FACTORY.createObstacle("box", uiPositionLeft, 1);
                    Obstacle obstacleR = OBSTACLE_FACTORY.createObstacle("box", uiPositionRight, 1);
                    obstacleList.add(obstacleL);
                    obstacleList.add(obstacleR);
                }

            }

            //System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());



//            for (int neighbour: maze.neighbors(i)){
//                Vector3d n= (Vector3d) maze.getValue(neighbour);
//
//                for (int x= (int) v.get_x()+1; x<n.get_x()+1; x++) {
//                    for (int z =(int) v.get_z()-1; z<n.get_z()-1; z++) {
////                        Vector3d currentPosX = (Vector3d) maze.getValue(x);
////                        Vector3d currentPosZ = (Vector3d) maze.getValue(z);
//                        Vector3f boxPos = new Vector3f(x, (float) function2d.evaluate(x, z), (float) z);
//
//                        Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
//                        obstacleList.add(obstacle);
//                    }
//                }
//                for (int x= (int) v.get_x()-1; x<n.get_x()-1; x++) {
//                    for (int z =(int) v.get_z()+1; z<n.get_z()+1; z++) {
////                        Vector3d currentPosX = (Vector3d) maze.getValue(x);
////                        Vector3d currentPosZ = (Vector3d) maze.getValue(z);
//                        Vector3f boxPos = new Vector3f(x, (float) function2d.evaluate(x, z), (float) z);
//
//                        Obstacle obstacle = OBSTACLE_FACTORY.createObstacle("box", boxPos, 1);
//                        obstacleList.add(obstacle);
//                    }
//                }
//            }
        }

    }

    public static void main(String[] args) {

    }
}
