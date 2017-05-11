package algorithm;

import java.util.*;

import data.*;

public class TSP {

	private int V;
	private List<Vertex> vertices;
	private int startVertex;
	private List<Integer> tour;
	private HashMap<List<Integer>, Double> tourMap;
	int counter = 0;

	public TSP(int V, List<Vertex> vertices) {
		this.V = V;
		this.vertices = vertices;

		tour = new ArrayList<>();
		tourMap = new HashMap<>();
	}

	public void tspTouren(int start) {
		boolean[] visited = new boolean[V];
		double cost = 0;
		startVertex = start;
		visited[start] = true;
		tour.add(start);

		rekTsp(visited, start, cost);
	}

	public void rekTsp(boolean visited[], int start, double cost) {
		boolean check = true;
		for (Edge edge : vertices.get(start).edges) {
			if (!visited[edge.getDest()]) {
				tour.add(edge.getDest());
				visited[edge.getDest()] = true;
				cost += edge.getWeight();
				rekTsp(visited, edge.getDest(), cost);
				for (boolean b : visited) {
					if (!b) {
						check = false;
						break;
					}
				}

				if (check) {
					List<Integer> temptour = new ArrayList<>();
					temptour.addAll(tour);
					// HashMap -> key für Kosten überdenken (Überschreibt Liste,
					// falls Kosten schon vorhanden.

					for (Edge edge2 : vertices.get(edge.getDest()).edges) {
						if (edge2.getDest() == startVertex) {
							temptour.add(startVertex);
							cost += edge2.getWeight();
							tourMap.put(temptour, cost);
							cost -= edge2.getWeight();
							break;
						}
					}
					
				}
				visited[edge.getDest()] = false;
				tour.remove(tour.size() - 1);
				cost -= edge.getWeight();
			}
		}

	}

	public void printTourandCost() {
		for (Map.Entry<List<Integer>, Double> tour : tourMap.entrySet()) {
			List<Integer> tourList = tour.getKey();
			Double cost = tour.getValue();
			System.out.print("Tour: ");
			for (Integer integer : tourList) {
				System.out.print(integer + " ");
			}
			System.out.println();
			System.out.println("Cost: " + cost);
			System.out.println();

		}
	}

}
