package dijkstra;

import java.util.*;

public class Dijkstra {

	public static HashMap<Integer, Double> dijkstra(Graph g, int source) {
		int n = g.nodeCount();
		HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(n);

		// Initializations
		for (int i = 0; i < n; i++)
			dist.put(i, Double.POSITIVE_INFINITY);
		dist.put(source, 0.0);
		pq.insert(source, 0.0);

		HashMap<Integer, Double> cloud = new HashMap<Integer, Double>();
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			cloud.put(v, dist.get(v));

			int[] neighbors = g.neighbors(v);
			for (int i = 0; i < neighbors.length; i++) {
				double alt;
				int w = neighbors[i];
				alt = dist.get(v) + g.weight(v, w);
				if (alt < dist.get(w)) {
					dist.put(w, alt);
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
