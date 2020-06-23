package dijkstra;

import java.util.*;

public class Dijkstra {

	/**
	 * Calculate the shortest path in the graph from a source to a destination
	 * @param g Graph
	 * @param source Vertex 1 integer
	 * @param destination Vertex 2 integer
	 * @return The shortest path
	 */

	public static int[] getShortestPath(Graph g, int source, int destination) {

		int n = g.nodeCount();
		double[] dist = new double[n];
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(n);

		// Initializations
		int[] prev = new int[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
			prev[i] = -1;
		}
		dist[source] = 0;
		pq.insert(source, 0.0);

		while (!pq.isEmpty()) {
			int v = pq.delMin();

			if (v == destination)
				break;

			int[] neighbors = g.neighbors(v);
			for (int i = 0; i < neighbors.length; i++) {
				double alt;
				int w = neighbors[i];
				alt = dist[v] + g.weight(v, w);
				if (alt < dist[w]) {
					dist[w] = alt;
					prev[w] = v;
					if (pq.contains(w))
						pq.decreaseKey(w, alt);
					else
						pq.insert(w, alt);
				}
			}
		}

		Stack<Integer> reverse = new Stack<Integer>();
		int u = destination;
		if (prev[u] != -1 || u == source) {
			while (u != -1) {
				reverse.add(u);
				u = prev[u];
			}
		}

		int[] path = new int[reverse.size()];
		int i = 0;
		while (!reverse.isEmpty()) {
			path[i++] = reverse.pop();
		}

		return path;
	}


	public static HashMap<Integer, Double> dijkstra(Graph g, int source) {
		int n = g.nodeCount();
		double[] dist = new double[n];
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(n);

		// Initializations
		for (int i = 0; i < n; i++)
			dist[i] = Double.POSITIVE_INFINITY;
		dist[source] = 0;
		pq.insert(source, 0.0);

		HashMap<Integer, Double> cloud = new HashMap<Integer, Double>();
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			cloud.put(v, dist[v]);

			int[] neighbors = g.neighbors(v);
			for (int i = 0; i < neighbors.length; i++) {
				double alt;
				int w = neighbors[i];
				alt = dist[v] + g.weight(v, w);
				if (alt < dist[w]) {
					dist[w] = alt;
					if (pq.contains(w))
						pq.decreaseKey(w, alt);
					else
						pq.insert(w, alt);
				}
			}
		}

		return cloud;
	}

}
