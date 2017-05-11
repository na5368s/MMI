package algorithm;
import data.*;
import java.util.*;

public class DoubleTree {

	Prim prim;
	DFS dfs;
	private int V;
	private List<Vertex> vertices;
	private boolean gerichtet;
	private double treecost = 0;
	private List<Integer> tour;
	
	public DoubleTree(int V, List<Vertex> vertices, boolean gerichtet){
		this.V = V;
		this.vertices = vertices;
		this.gerichtet = gerichtet;
	}
	 
	public Graph doubleTree(int start){
		Graph graph = new Graph(gerichtet);
		
		Vertex v;

		for (int i = 0; i < V; i++) {
			v = new Vertex(i);
			graph.vertices.add(v);
		}
		
		Graph graphtemp = new Graph(gerichtet);
		prim = new Prim(V, vertices);
		graphtemp = prim.primMST(start);
		dfs = new DFS(graphtemp.vertices);
				
		//graphtemp.recDepthSearchBefore(start);
		dfs.recDepthSearchBefore(start);
		tour = new ArrayList<>();
		
		int src = 0, dest = 0;
		boolean check = false;
		tour.add(start);
		for(int i=0; i<dfs.getDfsRoute().size()-1; i++){
			src = dfs.getDfsRoute().get(i);
			dest = dfs.getDfsRoute().get(i+1);
			
			for (Edge edge : graphtemp.vertices.get(src).edges) {
				if(edge.getDest() == dest){
					check = true;
					tour.add(dest);
					treecost += edge.getWeight();
					graph.vertices.get(src).addEdge(edge);
					
//					graph.vertices.get(edge.getDest())
//					.addEdge(new Edge(edge.getDest(), edge.getSrc(), edge.getWeight()));
					break;
				}
			}
			
			if(!check){
				for (Edge edge : this.vertices.get(src).edges) {
					if(edge.getDest() == dest){
						tour.add(dest);
						treecost += edge.getWeight();
						graph.vertices.get(src).addEdge(edge);
						
//						graph.vertices.get(edge.getDest())
//						.addEdge(new Edge(edge.getDest(), edge.getSrc(), edge.getWeight()));
						break;
					}
				}
			}
			
			check = false;
		}
		
		for (Edge edge : this.vertices.get(dest).edges) {
			if(edge.getDest() == start){
				tour.add(start);
				treecost += edge.getWeight();
				graph.vertices.get(dest).addEdge(edge);
				
//				graph.vertices.get(edge.getDest())
//				.addEdge(new Edge(edge.getDest(), edge.getSrc(), edge.getWeight()));
				break;
			}
		}
		
		return graph;
	}
	
	public void printTour(){
		System.out.print("Tour: ");
		for (Integer integer : tour) {
			System.out.print(integer + " ");
		}
		System.out.println();
	}
	
	public List<Integer> getTour(){
		return tour;
	}
	
	public double getOptimalCost(){
		return treecost;
	}
}
