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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeLoader {


    public static Graph loadMaze(String maze_design, PuttingCourse course) {
        Graph maze = ReadFile.setCoordinates(maze_design, course);
        return maze;
    }

    // test
    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("x + y"), new Vector3d(0, 10));

        List<String> allMazes = new ArrayList<String>();
        allMazes.add("src/main/java/dijkstra/maze-on-course");
        allMazes.add("src/main/java/dijkstra/maze-on-course2");
        allMazes.add("src/main/java/dijkstra/maze-on-course3");

        String chosen = pickOneMaze(allMazes);

        Graph maze = loadMaze(chosen, course);
        ((GraphAL) maze).print();

    }

    public static String pickOneMaze(List<String> allMazes) {
        Random rand = new Random();
        return allMazes.get(rand.nextInt(allMazes.size()));
    }

}
