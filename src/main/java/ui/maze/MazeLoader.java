package ui.maze;

import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;
import gameelements.PuttingCourse;
import org.joml.Vector3f;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;
import ui.entities.obstacles.Box;
import ui.entities.obstacles.Tree;
import ui.objloader.Vertex;

public class MazeLoader {

    public static Graph loadMaze(String maze_design, PuttingCourse course) {
        Graph maze = ReadFile.setCoordinates(maze_design, course);
        return maze;
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("x + y"), new Vector3d(0, 10));
        Graph maze = loadMaze("src/main/java/dijkstra/maze-on-course", course);
        ((GraphAL) maze).print();

    }

    public void buildMaze(GraphAL maze){
        for (int i = 0; i < maze.nodeCount(); i++) {
            Vector3d v = (Vector3d) maze.getValue(i);
            //System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());

            Vector3f pos1= new Vector3f((float) v.get_x()+2,(float)v.get_y(), (float) v.get_z()+2);
            Vector3f pos2= new Vector3f((float) v.get_x()-2,(float)v.get_y(), (float) v.get_z()-2);

            for (int neighbour: maze.neighbors(i)){
                Vector3d n= (Vector3d) maze.getValue(neighbour);

            }
        }

        for(int j=0; j<maze.nodeCount(); j++){

        }

    }
}
