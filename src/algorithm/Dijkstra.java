package algorithm;

import java.util.*;

import data.*;

public class Dijkstra {
	private int V;
	private List<Vertex> vertices;
	private boolean gerichtet;
	private List<Vertex> tour;

	public Dijkstra(int V, List<Vertex> vertices, boolean gerichtet) {
		this.V = V;
		this.vertices = vertices;
		this.gerichtet = gerichtet;
		tour = new ArrayList<>();
	}

	public void setVertices(List<Vertex> vertices){
		this.vertices = vertices;
	}

	public void setEdgesWeightEqual(){
		for (Vertex v:vertices
			 ) {
			for (Edge e:v.edges
				 ) {
				e.setWeight(1);
			}
		}
	}
	/**
	 *
	 * @param start - starting vertex value
	 * @return - graph with modified distances and predecessors for each vertex
	 */
	public Graph getSmallestRoute(int start) {
		Graph graph = new Graph(gerichtet);
		boolean visited[] = new boolean[V];
		Vertex v;
		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			v.setDistance(Double.POSITIVE_INFINITY);
			graph.vertices.add(v);
		}

		visited[start] = true;
		graph.vertices.get(start).setDistance(0);
		graph.vertices.get(start).setPrev(graph.vertices.get(start));

		int index = start;
		double min = Double.POSITIVE_INFINITY;

		double distance = 0;

		for(int i=0; i<V-1; i++){
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
						graph.vertices.get(edge.getDest()).setPrev(graph.vertices.get(index));
					}

				}
			}

			for (boolean b : visited) {
				if(!b){
					min = Double.POSITIVE_INFINITY;
					break;
				}
			}
		}
		
		return graph;
	}

	public List<Vertex> getTour(){
		return this.tour;
	}

	public boolean checkTour(Graph graph, int ende, int start){
		boolean check = false;
		if(start != ende){
			if(graph.vertices.get(ende).getPrev() != null) {
				recTour(graph, graph.vertices.get(ende).getPrev().getData(), start);
				tour.add(graph.vertices.get(ende));
			}

		}else{
			tour.add(graph.vertices.get(ende));
		}

		for (Vertex i:tour
			 ) {
			if(i.getData() == ende){
				check = true;
				return check;
			}
		}

		return check;
	}

	public void recTour(Graph graph, int start, int ende){
		if(start != ende){
			if(graph.vertices.get(start).getPrev() != null) {
				recTour(graph, graph.vertices.get(start).getPrev().getData(), ende);
				tour.add(graph.vertices.get(start));
			}

		}else{
			tour.add(graph.vertices.get(start));
		}
	}
	
	public void printRouteandDistance(Graph graph, int ende, int start){
		if(start != ende){
			rec(graph, graph.vertices.get(ende).getPrev().getData(), start);
			System.out.println(ende);
			
		}else{
			System.out.println(ende);
		}
		
		System.out.println("Distance = " + graph.vertices.get(ende).getDistance());
		
	}
	
	// R�ckw�rts durchlaufen, da startwert = endwert am Anfang. Daher mit endwert r�ckw�rts laufen
	public void rec(Graph graph, int start, int ende){
		if(start != ende){
			rec(graph, graph.vertices.get(start).getPrev().getData(), ende);
			System.out.print(start + " ");

		}else{
			System.out.print(ende + " ");
		}
	}
}
