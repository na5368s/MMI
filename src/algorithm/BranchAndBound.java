package algorithm;

import java.util.*;

import data.Edge;
import data.Vertex;

public class BranchAndBound {
	private DoubleTree doubleTree;
	private int V;
	private List<Vertex> vertices;
	private List<Integer> tour;
	private int startVertex;
	private double min;
	private List<Integer> tourOptimal;

	public BranchAndBound(int V, List<Vertex> vertices, boolean gerichtet) {
		this.V = V;
		this.vertices = vertices;
		tour = new ArrayList<>();
		tourOptimal = new ArrayList<>();
		doubleTree = new DoubleTree(V, vertices, gerichtet);
	}

	public void tspTouren(int start) {
		boolean[] visited = new boolean[V];
		double cost = 0;
		doubleTree.doubleTree(start);
		min = doubleTree.getOptimalCost();
		tourOptimal = doubleTree.getTour();

		visited[start] = true;
		startVertex = start;
		tour.add(start);

		rekTsp(visited, start, cost);
	}

	public void rekTsp(boolean visited[], int start, double cost) {

		for (Edge edge : vertices.get(start).edges) {
			if (!visited[edge.getDest()]) {
				tour.add(edge.getDest());
				visited[edge.getDest()] = true;
				cost += edge.getWeight();

				if (cost < min) {
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
				}

				visited[edge.getDest()] = false;
				tour.remove(tour.size() - 1);
				cost -= edge.getWeight();
			}
		}
	}

	public void printBranchAndBoundTourAndCost(){
		System.out.print("Tour: ");
		for (Integer integer : tourOptimal) {
			System.out.print(integer + " ");
		}
		System.out.println();
		System.out.println("Minimum Cost: " + min);


	}
}
