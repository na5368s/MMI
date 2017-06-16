package data;

import algorithm.*;
import java.io.*;
import java.util.*;

public class Graph {

	private DFS dfs;
	private BFS bfs;
	private Prim prim;
	private Kruskal kruskal;
	private NearestNeighbour nearestNeighbour;
	private DoubleTree doubleTree;
	private TSP tsp;
	private BranchAndBound bab;
	private Dijkstra dijkstra;
	private MooreBellmanFord mooreBellmanFord;
    private FordFulkerson fordFulkerson;
    private CycleCanceling cycleCanceling;
    private SuccessiveShortestPath successiveShortestPath;

	public List<Vertex> vertices;
	protected int V = 0;
	private int E = 0;
	protected boolean gerichtet = false;

	public Graph() {
		vertices = new ArrayList<Vertex>();
	}

	public Graph(boolean gerichtet) {
		vertices = new ArrayList<Vertex>();
		this.gerichtet = gerichtet;
	}

	public int getV() {
		return V;
	}

	public int getE() {
		return E;
	}

	/**
	 * Sets the data structure for the given Adjazenzmatrix
	 *
	 * @param file:
	 *            Graph.txt
	 * 
	 */
	public void setFromAdjazenzmatrix(String file) {
		Vertex vertex;
		int counter = 0;
		try {

			File input = new File(file);
			FileReader in = new FileReader(input);
			BufferedReader reader = new BufferedReader(in);
			V = Integer.parseInt(reader.readLine()); // erste Zeile
														// einlesen

			for (int i = 0; i < V; i++) {
				vertex = new Vertex(i);
				vertices.add(vertex);
			}

			while (reader.ready()) { // Jetzt kommen Zeilen der Form
										// 0 1 0 0 1
										// 1 0 0 1 0

				String s = reader.readLine();
				String[] kante = s.split("	"); // Trenne zwischen " "
												// Tabulator

				for (int j = 0; j < V; j++) {
					int v1 = Integer.parseInt(kante[j]);
					if (v1 != 0) {
						Edge edge = new Edge(counter, j, v1);
						vertices.get(counter).addEdge(edge);
						E++;
					}
				}
				counter++;
			}
			reader.close();
		}

		catch (IOException e) {
			System.out.println("Fehler beim Einlesen der Datei oder Datei nicht gefunden");
		}
	}

	/**
	 * Sets the data structure for the given EdgeList the Object of Graph will
	 * decide if the edges are directed or undirected if the file has three
	 * columns -> EdgeList is weighted
	 *
	 * @param file:
	 *            Graph.txt
	 * 
	 */
	public void setFromEdgeList(String file) {

		Vertex vertex;
		int counter = 0;

		try {

			File input = new File(file);
			FileReader in = new FileReader(input);
			BufferedReader reader = new BufferedReader(in);
			V = Integer.parseInt(reader.readLine()); // erste Zeile
														// einlesen
			for (int i = 0; i < V; i++) {
				vertex = new Vertex(i);
				vertices.add(vertex);
			}
			while (reader.ready()) { // Jetzt kommen Zeilen der Form
				// v1 v2 Gewicht
				String s = reader.readLine();
				String[] kante = s.split("	"); // Trenne zwischen " "
				// Tabulator

				if (kante.length < 2) {
					Double balance = Double.parseDouble(kante[0]);
					if(balance < 0){
						vertices.get(counter).setisSink();
					}
					vertices.get(counter++).setBalance(balance);
				} else {
					int v1 = Integer.parseInt(kante[0]); // hier wird v1(s.oben) in
					// ein Integer
					// umgewandelt

					int v2 = Integer.parseInt(kante[1]); // ."..v2..."...
					double gewicht = 0;
					double cost = 0;
					if (kante.length > 2) {
						if(kante.length == 4){
							cost = Double.parseDouble(kante[2]); // Kosten für Kante
							gewicht = Double.parseDouble(kante[3]);
						}else {
							gewicht = Double.parseDouble(kante[2]); // Und das Gewicht
							// wird ebenfalls in
							// ein Double
							// umgewandelt
						}

					}

					if (gerichtet) {
						Edge edge;
						edge = modifyEdge(kante, v1, v2, gewicht, cost);
						/*if (kante.length > 2) {
							if(kante.length == 3) {
								edge = new Edge(v1, v2, gewicht);
							}else {
								edge = new Edge(v1, v2, gewicht, cost);
							}
						} else {
							edge = new Edge(v1, v2);
						}*/
						vertices.get(v1).addEdge(edge);
						E++;
					} else {
						Edge edge;
						edge = modifyEdge(kante, v1, v2, gewicht, cost);
						/*if (kante.length > 2) {
							if(kante.length == 3) {
								edge = new Edge(v1, v2, gewicht);
							}else {
								edge = new Edge(v1, v2, gewicht, cost);
							}
						} else {
							edge = new Edge(v1, v2);
						}*/
						if (!vertices.get(v1).contains(edge)) {
							vertices.get(v1).addEdge(edge);
							E++;

							edge = modifyEdge(kante, v1, v2, gewicht, cost);
						/*if (kante.length > 2) {
							if(kante.length == 3) {
								edge = new Edge(v1, v2, gewicht);
							}else {
								edge = new Edge(v1, v2, gewicht, cost);
							}
						} else {
							edge = new Edge(v1, v2);
						}*/
							vertices.get(v2).addEdge(edge);
							E++;
						}
					}
				}
			}

			reader.close();
		}

		catch (IOException e) {
			System.out.println("Fehler beim Einlesen der Datei oder Datei nicht gefunden");
		}
	}

	public Edge modifyEdge(String[] kante, int v1, int v2, double gewicht, double cost){
		Edge edge;
		if (kante.length > 2) {
			if(kante.length == 3) {
				edge = new Edge(v1, v2, gewicht);
			}else {
				edge = new Edge(v1, v2, gewicht, cost);
			}
		} else {
			edge = new Edge(v1, v2);
		}
		return edge;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 1
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the number of connected Components in the given Graph
	 *
	 * @param methode:
	 *            1 -> iterativeBreadthSearch 2 -> recursiveDepthSearch
	 * @param start:
	 *            Vertex number to start with
	 * @return number of connected Components
	 */
	public int getNumberOfConnectedComponents(int methode, int start) {
		switch (methode) {
		case 1:
			bfs = new BFS(vertices);
			bfs.iterativeBreadthSearch(start);
			return bfs.getCounter();
		case 2:
			dfs = new DFS(vertices);
			dfs.recDepthSearchBefore(start);
			return dfs.getCounter();
		}

		return 0;
	}

	/**
	 * Prints the vertices of the connected Components in the sequence it was
	 * edited
	 *
	 * @param methode:
	 *            1 -> iterativeBreadthSearch 2 -> recursiveDepthSearch
	 */
	public void printConnectedComponents(int methode) {
		switch (methode) {
		case 1:
			for (Map.Entry<Integer, ArrayList<Vertex>> e : bfs.getPriorityqueueMap().entrySet()) {
				ArrayList<Vertex> queue = e.getValue();
				int number = e.getKey() + 1;
				System.out.println("Component number " + number + ":");
				for (Vertex vertex : queue) {
					System.out.print(vertex.getData() + " ");
				}
				System.out.println();
			}
			break;
		case 2:
			for (Map.Entry<Integer, ArrayList<Vertex>> e : dfs.getPriorityqueueMap().entrySet()) {
				ArrayList<Vertex> queue = e.getValue();
				int number = e.getKey() + 1;
				System.out.println("Component number " + number + ":");
				for (Vertex vertex : queue) {
					System.out.print(vertex.getData() + " ");
				}
				System.out.println();
			}
			break;
		}
	}

	public void clearVisited() {
		for (Vertex vertex : vertices) {
			vertex.undoVisited();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 2
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Graph getprimMST(int start) {
		prim = new Prim(V, vertices);

		return prim.primMST(start);
	}

	public Graph getkruskalMST() {
		kruskal = new Kruskal(V, vertices);

		return kruskal.kruskalMST();
	}

	public void printMSTMinimumCost(Graph graph) {
		double min = 0;
		List<Edge> edgeList = getEdges(graph.vertices);

		// Collections.sort(edgeList);
		// edgeList = removeDuplicate(edgeList);

		for (Edge edge : edgeList) {
			min = min + edge.getWeight();
		}

		System.out.println("Minimum Costs: " + min / 2);
	}

	public void printMST(Graph graph) {
		System.out.println("Vertex: \t Edges: \t Weight:");
		for (Vertex vertex : graph.vertices) {
			for (Edge edge : vertex.edges) {
				System.out.println(vertex.getData() + " \t\t " + edge.getSrc() + " -> " + edge.getDest() + " \t "
						+ edge.getWeight());
			}
		}
	}

	public List<Edge> getEdges(List<Vertex> vertexList) {
		List<Edge> edgeList = new ArrayList<Edge>();

		for (Vertex vertex : vertexList) {
			for (Edge edge : vertex.edges) {
				edgeList.add(edge);
			}
		}

		return edgeList;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 3
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Graph nextNeighbour(int start) {
		nearestNeighbour = new NearestNeighbour(V, vertices, gerichtet);

		Graph graph = new Graph(gerichtet);
		graph = nearestNeighbour.nextNeighbour(start);

		nearestNeighbour.printTour();
		System.out.println("Cost: " + nearestNeighbour.getOptimalCost());

		return graph;
	}

	public Graph DoubleTree(int start) {
		doubleTree = new DoubleTree(V, vertices, gerichtet);
		Graph graph = new Graph(gerichtet);
		graph = doubleTree.doubleTree(start);

		doubleTree.printTour();
		System.out.println("Cost: " + doubleTree.getOptimalCost());

		return graph;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 4
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void tspBruteForce(int start) {
		tsp = new TSP(V, vertices);
		tsp.tspTouren(start);
	}

	public void printTspTourandCost() {
		tsp.printTourandCost();
	}
	
	public void branchAndBound(int start){
		bab = new BranchAndBound(V, vertices, gerichtet);
		bab.tspTouren(start);
	}
	
	public void printBranchAndBoundTourAndCost() {
		bab.printBranchAndBoundTourAndCost();
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 5
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void dijkstra(int start, int ende){
		dijkstra = new Dijkstra(V, vertices, gerichtet);
		
		Graph graph = dijkstra.getSmallestRoute(start);
		dijkstra.printRouteandDistance(graph, ende, start);
		System.out.println();
	}

	public void moore_Bellman_Ford(int start, int ende){
		mooreBellmanFord = new MooreBellmanFord(V, vertices, gerichtet);

		Graph graph = mooreBellmanFord.getSmallestRoute(start);
		if(mooreBellmanFord.getCycle().isEmpty()) {
			mooreBellmanFord.printRouteandDistance(graph, ende, start);
		}else{
			mooreBellmanFord.printCycle();
		}
		System.out.println();
	}

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Algorithmus f�r Praktikum 6
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void fordFulkerson(int start, int ende){
        fordFulkerson = new FordFulkerson(V,vertices,gerichtet);

		System.out.println("Maximaler Flusswert: " + fordFulkerson.getMaxFlow(start, ende));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 7
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void cycleCanceling(){
		cycleCanceling = new CycleCanceling(V, vertices, gerichtet);
		System.out.println(cycleCanceling.getMinCostFlow());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus f�r Praktikum 8
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void successiveShortestPath(){
		successiveShortestPath = new SuccessiveShortestPath(V, vertices, gerichtet);
		System.out.println(successiveShortestPath.getMinCostFlow());
	}
}
