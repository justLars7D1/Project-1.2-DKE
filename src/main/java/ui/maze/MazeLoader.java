package ui.maze;

import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;

public class MazeLoader {

    public static Graph loadMaze(String maze_design) {
        Graph maze = ReadFile.setCoordinates(maze_design);
        return maze;
    }

    public static void main(String[] args) {
        Graph maze = loadMaze("src/main/java/dijkstra/maze-on-course");
        ((GraphAL) maze).print();
    }
}
