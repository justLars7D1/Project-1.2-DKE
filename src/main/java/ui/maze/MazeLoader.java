package ui.maze;

import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;
import gameelements.PuttingCourse;
import org.joml.Vector3f;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;
import ui.CourseDesignerScreen;
import ui.entities.Obstacle;
import ui.entities.obstacles.Box;
import ui.entities.obstacles.ObstacleFactory;
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

}
