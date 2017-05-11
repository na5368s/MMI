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

				int v1 = Integer.parseInt(kante[0]); // hier wird v1(s.oben) in
														// ein Integer
														// umgewandelt

				int v2 = Integer.parseInt(kante[1]); // ."..v2..."...
				double gewicht = 0;
				if (kante.length > 2) {
					gewicht = Double.parseDouble(kante[2]); // Und das Gewicht
															// wird ebenfalls in
															// ein Double
															// umgewandelt

				}

				if (gerichtet) {
					Edge edge;
					if (kante.length > 2) {
						edge = new Edge(v1, v2, gewicht);
					} else {
						edge = new Edge(v1, v2);
					}
					vertices.get(v1).addEdge(edge);
					E++;
				} else {
					Edge edge;
					if (kante.length > 2) {
						edge = new Edge(v1, v2, gewicht);
					} else {
						edge = new Edge(v1, v2);
					}
					if (!vertices.get(v1).contains(edge)) {
						vertices.get(v1).addEdge(edge);
						E++;

						if (kante.length > 2) {
							edge = new Edge(v2, v1, gewicht);
						} else {
							edge = new Edge(v2, v1);
						}
						vertices.get(v2).addEdge(edge);
						E++;
					}
				}
			}

			reader.close();
		}

		catch (IOException e) {
			System.out.println("Fehler beim Einlesen der Datei oder Datei nicht gefunden");
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus für Praktikum 1
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
	// Algorithmus für Praktikum 2
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
	// Algorithmus für Praktikum 3
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Graph nextNeighbour(int start) {
		nearestNeighbour = new NearestNeighbour(V, vertices, gerichtet);

		Graph graph = new Graph(gerichtet);
		graph = nearestNeighbour.nextNeighbour(start);

		//Als eigene Methode in der Klasse implementieren
		nearestNeighbour.printTour();
		System.out.println("Cost: " + nearestNeighbour.getOptimalCost());

		return graph;
	}

	public Graph DoubleTree(int start) {
		doubleTree = new DoubleTree(V, vertices, gerichtet);
		Graph graph = new Graph(gerichtet);
		graph = doubleTree.doubleTree(start);

		//Als eigene Methode in der Klasse implementieren
		doubleTree.printTour();
		System.out.println("Cost: " + doubleTree.getOptimalCost());

		return graph;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Algorithmus für Praktikum 4
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
	// Algorithmus für Praktikum 5
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void dijkstra(int start, int ende){
		dijkstra = new Dijkstra(V, vertices, gerichtet);
		
		Graph graph = dijkstra.getSmallestRoute(start);
		System.out.println();
		dijkstra.printRouteandDistance(graph, ende, start);
		System.out.println();
	}
}
