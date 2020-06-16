package dijkstra;
import physicsengine.Vector3d;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {
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

    public static Graph setCoordinates(String fileName) {
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
                G.setValue(i, new Vector3d(x, z));
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

    private static double setWeight(Graph g, int v, int w) {
        Vector3d vv = ((Vector3d) g.getValue(v)).copy();
        Vector3d ww = ((Vector3d) g.getValue(w)).copy();
        double weight = Math.pow(Math.pow(vv.get_x() - ww.get_x(), 2) + Math.pow(vv.get_z() - ww.get_z(), 2), 0.5);
        return weight;
    }

    public static void main(String[] args) {
        Graph g = setCoordinates("src/main/java/dijkstra/maze-on-course") ;
        ((GraphAL) g).print();
    }
}