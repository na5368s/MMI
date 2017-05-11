package algorithm;
import data.*;
import java.util.*;

public class Prim {
	
	private int V;
	private List<Vertex> vertices;
	
	public Prim(int V, List<Vertex> vertices){
		this.V = V;
		this.vertices = vertices;
	}

	public Graph primMST(int start) {
		Graph graphorig = new Graph(false);
		boolean visited[] = new boolean[V];
		Vertex v;

		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			graphorig.vertices.add(v);
		}

		PriorityQueue<Edge> queue = new PriorityQueue<>();

		queue.addAll(vertices.get(start).edges);
		visited[start] = true;

		Edge temp = null;

		while (!queue.isEmpty()) {
			temp = queue.poll();

			if (!visited[temp.getDest()]) {
				queue.addAll(vertices.get(temp.getDest()).edges);
				graphorig.vertices.get(temp.getSrc()).addEdge(temp);

				// Kante auch für entgegengesetzten Knoten setzen.
				graphorig.vertices.get(temp.getDest())
						.addEdge(new Edge(temp.getDest(), temp.getSrc(), temp.getWeight()));
				visited[temp.getDest()] = true;
			}
		}

		return graphorig;
	}
}
