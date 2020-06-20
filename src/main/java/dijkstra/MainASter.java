package dijkstra;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;

import java.util.HashMap;
import java.util.List;

public class MainASter {
    public static void main(String[] args) {
        //Graph g0 = ReadFile.read("src/main/java/dijkstra/maze");
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("0.0000001*x^2 + 0.0000001*y^2"), new Vector3d(0, 10));
        Graph g0 = ReadFile.setCoordinates("src/main/java/dijkstra/maze-on-course", course);
        GraphAL g = ((GraphAL) g0);
        g.print();
        System.out.println();
        for (int i = 0; i < g.nodeCount(); i++) {
            Vector3d v = (Vector3d) g.getValue(i);
            System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());
        }
        System.out.println();
        getShortestPathTo(g, 6, 0);
        getShortestPathTo(g, 6, 1);
        getShortestPathTo(g, 6, 3);
        getShortestPathTo(g, 6, 4);
        getShortestPathTo(g, 6, 5);
        getShortestPathTo(g, 6, 6);
        getShortestPathTo(g, 6, 7);
        getShortestPathTo(g, 6, 8);
        getShortestPathTo(g, 5, 0);
        getShortestPathTo(g, 5, 1);
        getShortestPathTo(g, 5, 3);
        getShortestPathTo(g, 5, 4);
        getShortestPathTo(g, 5, 5);
        getShortestPathTo(g, 5, 6);
        getShortestPathTo(g, 5, 7);
        getShortestPathTo(g, 5, 8);
    }

    public static void getShortestPathTo(Graph g, int source, int destination) {
        List<Integer> path = ASter.aSter(g,source, destination);
        System.out.print(source + " to " + destination + ": ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i) + " ");
        }
        System.out.println();
    }
}
