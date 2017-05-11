package algorithm;

import java.util.*;

import data.*;

public class Dijkstra {
	private int V;
	private List<Vertex> vertices;
	private boolean gerichtet;

	public Dijkstra(int V, List<Vertex> vertices, boolean gerichtet) {
		this.V = V;
		this.vertices = vertices;
		this.gerichtet = gerichtet;
	}

	public Graph getSmallestRoute(int start) {
		Graph graph = new Graph(gerichtet);
		boolean visited[] = new boolean[V];
		Vertex v;
		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			graph.vertices.add(v);
		}

		visited[start] = true;
		graph.vertices.get(start).setDistance(0);
		graph.vertices.get(start).setPrev(start);

		int index = start;
		double min = Double.MAX_VALUE;
		
		boolean check = false;
		double distance = 0;
		
		while (!check) {
			for (Vertex vertex : graph.vertices) {
				if (!visited[vertex.getData()] && vertex.getDistance() < min) {
					index = vertex.getData();
					min = vertex.getDistance();
				}
			}
			visited[index] = true;

			for (Edge edge : vertices.get(index).edges) {
				if (!visited[edge.getDest()]) {
					distance = graph.vertices.get(index).getDistance() + edge.getWeight();
					if(distance < graph.vertices.get(edge.getDest()).getDistance()){
						graph.vertices.get(edge.getDest()).setDistance(distance);
						graph.vertices.get(edge.getDest()).setPrev(index);
					}
					
				}
			}
			
			check = true;
			for (boolean b : visited) {
				if(!b){
					check = false;
					min = Double.MAX_VALUE;
					break;
				}
			}
		}
		
		return graph;
	}
}
