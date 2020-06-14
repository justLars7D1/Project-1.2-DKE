package ui.maze;

import dijkstra.Dijkstra;
import dijkstra.Graph;
import dijkstra.GraphAL;
import dijkstra.ReadFile;
import org.joml.Vector3f;
import ui.entities.obstacles.Wall;

/*
First idea so not applicable yet, missing some methods to access the edges
 */


import java.util.HashMap;

public class MazeBuilder {
    Graph graph = ReadFile.read("src\\ui\\maze\\UsersMaze");
    GraphAL g = ((GraphAL) graph);
    HashMap<Integer, Double> distancesTo = Dijkstra.dijkstra(graph, 2);

   //for each edge, create two walls that will be put on each side


    public static void createWall(Graph g){
        for (int i= 0; i<g.edgeCount(); i++){
            Wall wall1= new Wall(new Vector3f(/* edge(i).getPosition().getX()+2, edge(i).getPosition().getY(), edge(i).getPosition().getZ()*/), 1);
            Wall wall2= new Wall(new Vector3f(/* edge(i).getPosition().getX()-2, edge(i).getPosition().getY(), edge(i).getPosition().getZ()*/), 1);
        }
    }

}
