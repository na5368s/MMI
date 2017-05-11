package algorithm;
import data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DFS {

	private List<Integer> dfsroute;
	private HashMap<Integer, ArrayList<Vertex>> priorityqueueMap = new HashMap<>();
	private int counter = 1;
	private List<Vertex> vertices;
	
	public DFS(List<Vertex> vertices){
		this.vertices = vertices;
	}
	
	public List<Integer> getDfsRoute(){
		return dfsroute;
	}
	
	public HashMap<Integer, ArrayList<Vertex>> getPriorityqueueMap(){
		return priorityqueueMap;
	}
	
	public int getCounter(){
		return counter;
	}
	
	/*
	 * Rekursive Tiefensuche zweite aufrufende Methode
	 */

	public void recDepthSearch(ArrayList<Vertex> queue, ArrayList<Vertex> priorityqueue) {
		int index;
		while (!queue.isEmpty()) {
			Vertex temp = queue.remove(0);
			// System.out.print(temp.getData() + " ");
			priorityqueue.add(temp);
			dfsroute.add(temp.getData());
			for (Edge edge : temp.edges) {
				index = edge.getDest();
				if (!vertices.get(index).isVisited()) {
					vertices.get(index).setVisited();
					recDepthSearch(queue, priorityqueue);
					queue.add(vertices.get(index));
				}
			}
		}
	}

	/*
	 * Rekursive Tiefensuche erste aufrufende Methode
	 */

	public void recDepthSearchBefore(int start) {
		ArrayList<Vertex> queue = new ArrayList<>();
		ArrayList<Vertex> priorityqueue = new ArrayList<>();
		dfsroute = new ArrayList<>();

		vertices.get(start).setVisited();
		queue.add(vertices.get(start));
		ArrayList<Vertex> queue2 = new ArrayList<>();
		recDepthSearch(queue, priorityqueue);
		for (Vertex vertex : priorityqueue) {
			queue2.add(vertex);
		}
		priorityqueueMap.put(counter - 1, queue2);
		priorityqueue.clear();
		for (Vertex vertex : vertices) {
			if (!vertex.isVisited()) {
				counter++;
				recDepthSearchBefore(vertex.getData());
				break;
			}
		}
	}
}
