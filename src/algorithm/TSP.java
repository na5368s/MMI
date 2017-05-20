package algorithm;

import java.util.*;

import data.*;

public class TSP {

	private int V;
	private List<Vertex> vertices;
	private int startVertex;
	private List<Integer> tour;
	private double min = Double.MAX_VALUE;
	//private HashMap<List<Integer>, Double> tourMap;
	private List<Integer> tourOptimal;
	int counter = 0;

	public TSP(int V, List<Vertex> vertices) {
		this.V = V;
		this.vertices = vertices;

		tour = new ArrayList<>();
		//tourMap = new HashMap<>();
		tourOptimal = new ArrayList<>();
	}

	/**
	 * Sets the starting vertex for the tour and calls the recursive TSP method
	 * @param start - starting index for Vertex
	 */
	public void tspTouren(int start) {
		boolean[] visited = new boolean[V];
		double cost = 0;
		startVertex = start;
		visited[start] = true;
		tour.add(start);

		rekTsp(visited, start, cost);
	}

	/**
	 * for each non visited destination of edge, the method will be called recursively
	 * and the index of vertex will be set the route.
	 * the costs of the edge will be added the the param cost
	 * For each Route - If all Vertices are visited, the route will be added to the tourMap
	 * @param visited - check if vertex is visited
	 * @param start - the actual index of vertex in the route
	 * @param cost - actual costs at the vertex of the route
	 */
	public void rekTsp(boolean visited[], int start, double cost) {
		for (Edge edge : vertices.get(start).edges) {
			if (!visited[edge.getDest()]) {
				tour.add(edge.getDest());
				visited[edge.getDest()] = true;
				cost += edge.getWeight();
				rekTsp(visited, edge.getDest(), cost);
				boolean check = true;
				for (boolean b : visited) {
					if (!b) {
						check = false;
						break;
					}
				}

				if (check) {

					Edge ptr = vertices.get(edge.getDest()).getEdge(edge.getDest(), startVertex);
					cost += ptr.getWeight();
					if(cost < min){
						tourOptimal.clear();
						tourOptimal.addAll(tour);
						tourOptimal.add(startVertex);
						min = cost;
					}
					cost -= ptr.getWeight();
				}
				visited[edge.getDest()] = false;
				tour.remove(tour.size() - 1);
				cost -= edge.getWeight();
			}

		}

	}

	public void printTourandCost() {
		System.out.print("Tour: ");
		for (Integer integer : tourOptimal) {
			System.out.print(integer + " ");
		}
		System.out.println("Cost: " + min);
//		for (Map.Entry<List<Integer>, Double> tour : tourMap.entrySet()) {
//			List<Integer> tourList = tour.getKey();
//			Double cost = tour.getValue();
//			System.out.print("Tour: ");
//			for (Integer integer : tourList) {
//				System.out.print(integer + " ");
//			}
//			System.out.println();
//			System.out.println("Cost: " + cost);
//			System.out.println();
//
//		}
	}

}
