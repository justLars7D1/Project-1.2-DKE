package f;

import dijkstra.Dijkstra;
import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;
import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;

import java.util.HashMap;
import java.util.List;

public class MainFloyd {
    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("0.0000001*x^2 + 0.0000001*y^2"), new Vector3d(0, 10));
        Graph g0 = ReadFile.setCoordinates("src/main/java/dijkstra/maze-on-course", course);
        GraphAL g = ((GraphAL) g0);
        g.print();
        System.out.println();
        HashMap<Integer, Double> distancesTo = Dijkstra.dijkstra(g, 5);
        for (int v : distancesTo.keySet()) {
            System.out.println(v + "(" + distancesTo.get(v) + ") ");
        }
        for (int i = 0; i < g.nodeCount(); i++) {
            Vector3d v = (Vector3d) g.getValue(i);
            System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());
        }
        System.out.println();
        getDistanceTable(g);
        System.out.println();
        /*List<Integer> p= getShortest(g, 1, 3);
        for(int i=0; i<p.size(); i++){
            System.out.println(p.get(i));
        }

         */
    }

   /* private static List<Integer> getShortest(GraphAL g, int src, int dst) {
        List<Integer> path= getShortest(g, src, dst);

        return path;
    }
    */

    public static void getDistanceTable(Graph g) {
        double[][] table = FloydWarshall.distTable(g);

        for(int i=0; i<table.length; i++){
            for (int j=0; j<table[0].length; j++){
                System.out.print((int)table[i][j]+ "\t");
            }
            System.out.println();
        }

    }

}
