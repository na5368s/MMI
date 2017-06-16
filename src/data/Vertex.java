package data;
import java.util.*;

public class Vertex implements Cloneable{
	private boolean visited = false;
	private int data;
	private double distance = Double.MAX_VALUE;
	private Vertex prev;
	public List<Edge> edges;
	private double balance = 0;
	private double balancetemp = 0;
	boolean isSink = false;
	
	public Vertex(int data){
		this.data = data;
		edges = new ArrayList<Edge>();
	}
	
	Vertex(Vertex vertex){
		visited = vertex.visited;
		data = vertex.data;
		edges = new ArrayList<Edge>();
		for (Edge edge : vertex.edges) {
            edges.add(new Edge(edge));
        }
	}
	
	public boolean isVisited(){
		return visited;
	}
	
	public void setVisited(){
		visited = true;
	}
	
	public void undoVisited(){
		visited = false;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}
	
	public double getDistance(){
		return distance;
	}

	public void setBalance(double balance){
		this.balance += balance;
	}

	public double getBalance(){
		return balance;
	}

	public void setBalancetemp(double balancetemp){
		this.balancetemp += balancetemp;
	}

	public double getBalancetemp(){
		return balancetemp;
	}

	public void setisSink(){
		isSink = true;
	}

	public boolean isSink(){
		return isSink;
	}
	
	public void setPrev(Vertex prev){
		this.prev = prev;
	}
	
	public Vertex getPrev(){
		return prev;
	}
	
	public int getData(){
		return data;
	}
	
	public void addEdge(Edge edge){
		edges.add(edge);
	}
	
	public void removeEdge(Edge edge){
		edges.remove(edge);
	}
	
	public boolean checkEdge(Edge edgetemp){
		for (Edge edge : edges) {
			if(edge == edgetemp){
				return true;
			}
		}
		return false;
	}
	
	public Edge getEdge(int index){		
		return edges.get(index);
	}
	
	public boolean contains(Edge e){
		for (Edge edge : edges) {
			if(edge.getSrc() == e.getSrc() && edge.getDest()==e.getDest()){
				return true;
			}
		}
		return false;
	}
	
	public Edge getEdge(int src, int dest){
		for (Edge edge : edges) {
			if(edge.getSrc() == src && edge.getDest() == dest){
				return edge;
			}
		}
		return null;
	}
	
	public Vertex clone(){
		Vertex vertex = new Vertex(data);
		try {
			Edge edge;
			for (Edge edge2 : edges) {
				edge = new Edge(edge2.getSrc(),edge2.getDest(),edge2.getWeight());
				vertex.addEdge(edge);
			}		
			return (Vertex) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			throw new InternalError(e);
		}
	}
	
}
