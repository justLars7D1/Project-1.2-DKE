package dijkstra;
import java.util.*;

public class MainDijkstra {

	public static void main(String[] args) {
		Graph g0 = ReadFile.read("txt-graph");
		GraphAL g = ((GraphAL) g0);
		g.printGraph();
		int[] nb4 = g.neighbors(4);
		for (int i = 0; i < nb4.length; i++) {
			System.out.print(nb4[i] + " ");
		}
		System.out.println();
		for (int i = 0; i < nb4.length; i++) {
			System.out.print(g.weight(4, nb4[i]) + " ");
		}
		System.out.println();
		HashMap<Integer, Double> path = Dijkstra.dijkstra(g, 9);
		for (int v : path.keySet()) {
			System.out.print(v + "(" + path.get(v) + ") - ");
		}
	}

}
