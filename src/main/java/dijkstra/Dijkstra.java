package dijkstra;

import java.util.*;

public class Dijkstra {

	public static HashMap<Integer, Double> dijkstra(Graph g, int source) {
		int n = g.nodeCount(); //number of nodes in the graph
		HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
		PriorityQueue<double[]> queue = new PriorityQueue<double[]>(n, new NodeComparator());
		
		// Initializations
		dist.put(source, 0.0);
		double[] start = {source, 0.0};
		queue.add(start);
		for (int i = 0; i < n; i++) {
			if (i != source) {
				dist.put(i, Double.POSITIVE_INFINITY);
				double[] node = {i, Double.POSITIVE_INFINITY};
				queue.add(node);
			}
		}
		
		HashMap<Integer, Double> cloud = new HashMap<Integer, Double>();
		while (!queue.isEmpty()) {
				int v = (int) queue.poll()[0];
				cloud.put(v, dist.get(v));

				int[] neighbors = g.neighbors(v);
				for (int i = 0; i < neighbors.length; i++) {
					double alt;
					int w = neighbors[i];
					if (dist.containsKey(w)) {
						alt = dist.get(v) + g.weight(v, w);
						if (alt < dist.get(w))
							dist.put(w, alt);
					}
				}
				//queue.remove(v);
		}
		return cloud;
	}


	public static HashMap<Integer, Double> dijkstraForGame(Graph g, int source, int dest) {
		int n = g.nodeCount(); //number of nodes in the graph
		HashMap<Integer, Double> distance = new HashMap<Integer, Double>();
		PriorityQueue<double[]> queue = new PriorityQueue<double[]>(n, new NodeComparator());

		// Initializations
		distance.put(source, 0.0);
		double[] start = {source, 0.0};
		queue.add(start);
		for (int i = 0; i < n; i++) {
			if (i != source) {
				distance.put(i, Double.POSITIVE_INFINITY);
				double[] node = {i, Double.POSITIVE_INFINITY};
				queue.add(node);
			}
		}

		HashMap<Integer, Double> ans = new HashMap<Integer, Double>();
		while (!queue.isEmpty()) {
			for (int j=0; j<queue.size(); j++) {
				int v = (int) queue.poll()[j];
				if (v == dest) {
					ans.put(v, distance.get(v));
				}

				int[] neighbors = g.neighbors(v);
				for (int i = 0; i < neighbors.length; i++) {
					double alt;
					int w = neighbors[i];
					if (distance.containsKey(w)) {
						alt = distance.get(v) + g.weight(v, w);
						if (alt < distance.get(w))
							distance.put(w, alt);
					}
				}
			distance.remove(v);
			}
		}
		return ans;
	}
}
