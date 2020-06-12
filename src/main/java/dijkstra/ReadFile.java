package dijkstra;
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
                int weight = Integer.parseInt(s[2]);
                G.addEdge(v, w, weight);
                G.addEdge(w, v, weight);
            }
        } catch (IOException e){
        	System.out.println("No file to read!");
        }

        return G;
    }
}