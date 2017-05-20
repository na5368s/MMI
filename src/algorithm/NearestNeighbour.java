package algorithm;
import data.*;
import java.util.*;

public class NearestNeighbour {

	private int V;
	private List<Vertex> vertices;
	private boolean gerichtet;
	private List<Integer> tour;
	private double treecost = 0;

	public NearestNeighbour(int V, List<Vertex> vertices, boolean gerichtet) {
		this.V = V;
		this.vertices = vertices;
		this.gerichtet = gerichtet;
	}

	public List<Integer> getTour(){
		return tour;
	}

	/**
	 * adds the tour for the optimal route
	 * @param start - starting index of vertex
	 * @return Graph - contains the edges of Vertices visited
	 */
	public Graph nextNeighbour(int start) {
		Graph graphorig = new Graph(this.gerichtet);
		boolean visited[] = new boolean[V];
		Vertex v;
		tour = new ArrayList<>();

		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			graphorig.vertices.add(v);
		}

		PriorityQueue<Edge> queue = new PriorityQueue<>();

		queue.addAll(vertices.get(start).edges);
		visited[start] = true;

		tour.add(start);
		Edge temp = null;
		int counter = 1;
		//System.out.println("Edges: \t Weight:");
		while (counter != V) {
			temp = queue.poll();

			if (!visited[temp.getDest()]) {
				queue.clear();
				queue.addAll(vertices.get(temp.getDest()).edges);
				graphorig.vertices.get(temp.getSrc()).addEdge(temp);

				treecost += temp.getWeight();
				tour.add(temp.getDest());
				//System.out.println(temp.getSrc() + " -> " + temp.getDest() + "\t " + temp.getWeight());

				visited[temp.getDest()] = true;
				counter++;
			}
		}

		temp = queue.poll();
		queue.clear();
		Edge ptr = vertices.get(temp.getSrc()).getEdge(temp.getSrc(), start);
		treecost += ptr.getWeight();
		tour.add(ptr.getDest());
		//System.out.println(ptr.getSrc() + " -> " + ptr.getDest() + "\t " + ptr.getWeight());
		graphorig.vertices.get(temp.getSrc()).addEdge(ptr);

		return graphorig;
	}

	public void printTour(){
		System.out.print("Tour: ");
		for (Integer integer : tour) {
			System.out.print(integer + " ");
		}
		System.out.println();
	}

	public double getOptimalCost(){
		return treecost;
	}
}
