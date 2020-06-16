package dijkstra;

import java.util.HashMap;

public class MainDijkstra {

	public static void main(String[] args) {
		Graph g0 = ReadFile.read("src/main/java/dijkstra/maze");
		GraphAL g = ((GraphAL) g0);
		g.print();
		System.out.println();
		HashMap<Integer, Double> distancesTo = Dijkstra.dijkstra(g, 2);
		for (int v : distancesTo.keySet()) {
			System.out.println(v + "(" + distancesTo.get(v) + ") ");
		}
		System.out.println();
		getShortestPathTo(g, 2, 0);
		getShortestPathTo(g, 2, 1);
		getShortestPathTo(g, 2, 3);
		getShortestPathTo(g, 2, 4);
		getShortestPathTo(g, 2, 5);
		getShortestPathTo(g, 2, 6);
		getShortestPathTo(g, 2, 7);
	}

	public static void getShortestPathTo(Graph g, int source, int destination) {
		int[] shortestPathTo = Dijkstra.getShortestPath(g, source, destination);
		System.out.print(source + " to " + destination + ": ");
		for (int i = 0; i < shortestPathTo.length; i++) {
			System.out.print(shortestPathTo[i] + " ");
		}
		System.out.println();
	}

}
