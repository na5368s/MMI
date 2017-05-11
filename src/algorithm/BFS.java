package algorithm;
import data.*;
import java.util.*;

public class BFS {
	
	private HashMap<Integer, ArrayList<Vertex>> priorityqueueMap = new HashMap<>();
	private int counter = 1;
	private List<Vertex> vertices;
	
	public BFS(List<Vertex> vertices){
		this.vertices = vertices;
	}
	
	public HashMap<Integer, ArrayList<Vertex>> getPriorityqueueMap(){
		return priorityqueueMap;
	}
	
	public int getCounter(){
		return counter;
	}

	public void iterativeBreadthSearch(int start) {
		ArrayList<Vertex> queue = new ArrayList<Vertex>();
		ArrayList<Vertex> priorityqueue = new ArrayList<Vertex>();

		vertices.get(start).setVisited();
		queue.add(vertices.get(start));

		int index;
		while (!queue.isEmpty()) {
			Vertex temp = queue.remove(0);
			priorityqueue.add(temp);
			for (Edge edge : temp.edges) {
				index = edge.getDest();
				if (!vertices.get(index).isVisited()) {
					vertices.get(index).setVisited();
					queue.add(vertices.get(index));
				}
			}

			if (queue.isEmpty()) {
				ArrayList<Vertex> queue2 = new ArrayList<>();
				for (Vertex vertex : priorityqueue) {
					queue2.add(vertex);
				}
				priorityqueueMap.put(counter - 1, queue2);
				priorityqueue.clear();
				for (Vertex vertex : vertices) {
					if (!vertex.isVisited()) {
						counter++;
						vertex.setVisited();
						queue.add(vertex);
						break;
					}
				}
			}
		}
	}
}
