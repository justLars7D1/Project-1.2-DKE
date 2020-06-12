package dijkstra;

public class MainDijkstra {

	public static void main(String[] args) {
		Graph g = ReadFile.read("src/main/java/dijkstra/txt-graph");
		((GraphAL) g).printGraph();
	}
	
}
