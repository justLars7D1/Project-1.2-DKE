package dijkstra;
import gameelements.PuttingCourse;
import physicsengine.Vector3d;
import physicsengine.functions.FunctionParserRPN;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {

    /**
     * Construct the graph from a file
     * @param fileName File string
     * @return the graph
     */
    public static Graph read(String fileName){
        Graph G = null;
        try{
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            int vertices = sc.nextInt();
            sc.nextLine();
            int edges = sc.nextInt();
            sc.nextLine();
            G = new GraphAL();
            G.init(vertices);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] s = line.split(" ");
                int v = Integer.parseInt(s[0]);
                int w = Integer.parseInt(s[1]);
                double weight = Double.parseDouble(s[2]);
                G.addEdge(v, w, weight);
            }
        } catch (IOException e){
            System.out.println("No file to read!");
        }

        return G;
    }

    /**
     * Construct the graph from a file in function of a course
     * @param fileName File string
     * @param course PuttingCourse
     * @return the graph
     */

    public static Graph setCoordinates(String fileName, PuttingCourse course) {
        Graph G = null;
        try{
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            int vertices = sc.nextInt();
            sc.nextLine();
            int edges = sc.nextInt();
            sc.nextLine();
            G = new GraphAL();
            G.init(vertices);
            for (int i = 0; i < vertices; i++) {
                String line = sc.nextLine();
                String[] s = line.split(" ");
                int index = Integer.parseInt(s[0]);
                double x = Double.parseDouble(s[1]);
                double z = Double.parseDouble(s[2]);
                G.setValue(i, new Vector3d(x, course.get_height().evaluate(x, z) ,z));
            }
            for (int i = 0; i < edges; i++) {
                String line = sc.nextLine();
                String[] s = line.split(" ");
                int v = Integer.parseInt(s[0]);
                int w = Integer.parseInt(s[1]);
                double weight = setWeight(G, v, w);
                G.addEdge(v, w, weight);
            }
        } catch (IOException e){
            System.out.println("No file to read!");
        }

        return G;
    }

    /**
     * Calculate the weight between two vertices
     * @param g Graph
     * @param v Vertex1 integer
     * @param w Vertex2 integer
     * @return the weight
     */
    private static double setWeight(Graph g, int v, int w) {
        Vector3d vv = ((Vector3d) g.getValue(v)).copy();
        Vector3d ww = ((Vector3d) g.getValue(w)).copy();
        double weight = Math.pow(Math.pow(vv.get_x() - ww.get_x(), 2) + Math.pow(vv.get_z() - ww.get_z(), 2), 0.5);
        return weight;
    }

    public static void main(String[] args) {
        PuttingCourse course = new PuttingCourse(new FunctionParserRPN("x + y"), new Vector3d(0, 10));
        Graph g = setCoordinates("src/main/java/dijkstra/maze-on-course", course) ;
        ((GraphAL) g).print();
        for (int i = 0; i < g.nodeCount(); i++) {
            Vector3d v = (Vector3d) g.getValue(i);
            System.out.println("vertex " + i + " has x=" + v.get_x() + " y=" + v.get_y() + " z=" + v.get_z());
        }
    }
}