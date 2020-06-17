package dijkstra;

import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;

public class MainFW {
    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("x + y"), new Vector3d(0, 10));
        Graph g = ReadFile.setCoordinates("src/main/java/dijkstra/maze-on-course", course);
        ((GraphAL) g).print();
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
        int[] shortestPathTo = FloyWarshall.getShortestPath(g, source, destination);
        System.out.print(source + " to " + destination + ": ");
        for (int i = 0; i < shortestPathTo.length; i++) {
            System.out.print(shortestPathTo[i] + " ");
        }
        System.out.println();
    }
}
