package algorithm;
import data.*;
import java.util.*;

public class Kruskal {

	private int V;
	private List<Vertex> vertices;
	
	public Kruskal(int V, List<Vertex> vertices){
		this.V = V;
		this.vertices = vertices;
	}
	
	public Graph kruskalMST() {
		
		// List<Edge> result = new ArrayList<Edge>();
		Graph graphorig = new Graph(false);
		List<Edge> edgeList = getEdges(vertices);
		int path[] = new int[V];
		Vertex v;

		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			graphorig.vertices.add(v);
		}

		Collections.sort(edgeList);

		// edgeList = removeDuplicate(edgeList);

		// Initializing the path array
		for (int i = 0; i < V; i++) {
			path[i] = 0;
		}

		// Counts the number of edges selected or discarded
		int j = 0;

		while (j != edgeList.size()) {
			// System.out.print("Edge ("
			// + edgeList.get(j).getSrc() + ", " + edgeList.get(j).getDest() +
			// ") "
			// + "with weight " + edgeList.get(j).getWeight() + " ");
			if (checkCycle(edgeList.get(j), path)) {
				graphorig.vertices.get(edgeList.get(j).getSrc()).addEdge(edgeList.get(j));

				graphorig.vertices.get(edgeList.get(j).getDest()).addEdge(
						new Edge(edgeList.get(j).getDest(), edgeList.get(j).getSrc(), edgeList.get(j).getWeight()));

				// System.out.println("is selected");
			} else {
				// System.out.println("is discarded");
			}
			j++;
		}

		return graphorig;
	}

	public boolean checkCycle(Edge edge, int[] path) {
		int src = edge.getSrc(), dest = edge.getDest();

		while (path[src] > 0)
			src = path[src];

		while (path[dest] > 0)
			dest = path[dest];

		if (src != dest) {
			path[src] = dest;
			return true;
		}
		return false;
	}
	
	public List<Edge> getEdges(List<Vertex> vertexList) {
		List<Edge> edgeList = new ArrayList<Edge>();

		for (Vertex vertex : vertexList) {
			for (Edge edge : vertex.edges) {
				edgeList.add(edge);
			}
		}

		return edgeList;
	}
}
