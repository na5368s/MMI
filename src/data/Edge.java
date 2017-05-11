package data;

public class Edge implements Comparable<Edge>, Cloneable {
	private int src = 0;
	private int dest = 0;
	private double weight = 0;

	public Edge(int src, int dest) {
		this.src = src;
		this.dest = dest;
	}

	public Edge(int src, int dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}

	public Edge(Edge edge) {
		src = edge.src;
		dest = edge.dest;
		weight = edge.weight;
	}

	public int getSrc() {
		return src;
	}

	public int getDest() {
		return dest;
	}

	public double getWeight() {
		return weight;
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
