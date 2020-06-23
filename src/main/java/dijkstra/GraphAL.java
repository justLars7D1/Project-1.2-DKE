package dijkstra;

public class GraphAL implements Graph {

	private class Edge {
		int vertex;
		double weight;
		Edge prev, next;

		Edge(int v, double w, Edge p, Edge n) {
			vertex = v;
			weight = w;
			prev = p;
			next = n;
		}
	}

	private Edge[] nodeArray;
	public static Object[] nodeValues;// coordinators (x, y, z) correspond to the mesh
	private int numEdge;

	@Override
	public void init(int n) {
		nodeArray = new Edge[n];
		for (int i = 0; i < n; i++)
			nodeArray[i] = new Edge(-1, -1, null, null);
		nodeValues = new Object[n];
		numEdge = 0;
	}

	@Override
	public int nodeCount() {
		return nodeArray.length;
	}

	@Override
	public int edgeCount() {
		return numEdge;
	}

	@Override
	public Object getValue(int v) {
		return nodeValues[v];
	}

	@Override
	public void setValue(int v, Object val) {
		nodeValues[v] = val;
	}

	@Override
	public void addEdge(int v, int w, double wgt) {
		if (wgt == 0) return;
		Edge curr = find(v, w);
		if ((curr.next != null) && (curr.next.vertex == w)) {
			curr.next.weight = wgt;
		} else {
			curr.next = new Edge(w, wgt, curr, curr.next);
			if (curr.next.next != null) curr.next.next.prev = curr.next;
		}
		numEdge++;
	}

	// return the link in v's neighbor precedes the one with w
	private Edge find(int v, int w) {
		Edge edg = nodeArray[v];
		while ((edg.next != null) && (edg.next.vertex < w))
			edg = edg.next;
		return edg;
	}

	@Override
	public double weight(int v, int w) {
		Edge curr = find(v, w);
		if ((curr.next == null) || (curr.next.vertex != w)) return 0;
		else return curr.next.weight;
	}

	@Override
	public void removeEdge(int v, int w) {
		Edge curr = find(v, w);
		if ((curr.next == null) || curr.next.vertex != w) return;
		else {
			curr.next = curr.next.next;
			if (curr.next != null) curr.next.prev = curr;
		}
	}

	@Override
	public int[] neighbors(int v) {
		int cnt = 0;
		Edge curr;
		for (curr = nodeArray[v].next; curr != null; curr = curr.next)
			cnt++;
		int[] neighbors = new int[cnt];
		cnt = 0;
		for (curr = nodeArray[v].next; curr != null; curr = curr.next)
			neighbors[cnt++] = curr.vertex;
		return neighbors;
	}

	public void print() {
		for (int v = 0; v < nodeArray.length; v++) {
			System.out.print(v + ": ");
			Edge curr;
			for (curr = nodeArray[v].next; curr != null; curr = curr.next)
				System.out.print(curr.vertex + "[" + curr.weight + "] ");
			System.out.println();
		}
	}
}
