package dijkstra;

import java.util.HashMap;

public class MainDijkstra {

	public static void main(String[] args) {
		Graph g0 = ReadFile.read("src\\dijkstra\\maze");
		GraphAL g = ((GraphAL) g0);
		g.print();
		System.out.println();
		HashMap<Integer, Double> path = Dijkstra.dijkstra(g, 2);
		for (int v : path.keySet()) {
			System.out.print(v + "(" + path.get(v) + ") - ");
		}
/*
		System.out.println();
		System.out.println();

		HashMap<Integer, Double> shortest = Dijkstra.dijkstraForGame(g, 9, 0);
		for (int c : shortest.keySet()) {
			System.out.print(c + "(" + shortest.get(c) + ")");
		}
		System.out.println();
*/
	}
}
