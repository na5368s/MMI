package data;

public class Edge implements Comparable<Edge>, Cloneable {
	private int src = 0;
	private int dest = 0;
	private double weight = 0;
	private double capacity = 0;
	private double flow = 0;
	private double cost = 0;

	public Edge(int src, int dest) {
		this.src = src;
		this.dest = dest;
	}

	public Edge(int src, int dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.capacity = weight;
	}

	public Edge(int src, int dest, double weight, double cost) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.capacity = weight;
		this.cost = cost;
	}

	public Edge(Edge edge) {
		src = edge.src;
		dest = edge.dest;
		weight = edge.weight;
		capacity = edge.capacity;
		cost = edge.cost;
	}

	public int getSrc() {
		return src;
	}

	public int getDest() {
		return dest;
	}

	public void setWeight(double weight){
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public double getCapacity() { return capacity; }

	public void setFlow(double min){
		flow = flow + min;
	}

	public double getFlow(){
		return this.flow;
	}

	public void setCost(double cost){
		this.cost = cost;
	}

	public double getCost(){
		return cost;
	}

	public Edge clone() {
		try {
			return (Edge) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	@Override
	public int compareTo(Edge another) {
		return Double.compare(this.weight, another.getWeight());
	}

}
