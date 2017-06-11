package algorithm;

import data.*;

import java.util.*;

/**
 * Created by Anupkumar on 02.06.2017.
 */
public class FordFulkerson {
    private Dijkstra dijkstra;
    private int V;
    private List<Vertex> vertices;
    private boolean gerichtet;

    public FordFulkerson(int V, List<Vertex> vertices, boolean gerichtet) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
    }

    /**
     *
     * @param start - starting vertex
     * @param ende - ending vertex
     * @return - value for maxFlow
     */
    public double getMaxFlow(int start, int ende){
        Graph graph = new Graph(gerichtet);
        double maxFlow = 0;

        Vertex v;

        for (int i = 0; i < V; i++) {
            v = new Vertex(i);
            graph.vertices.add(v);
        }

        setEdges(graph.vertices);

        dijkstra = new Dijkstra(V, graph.vertices, gerichtet);
        dijkstra.setEdgesWeightEqual();
        List<Vertex> tour;
        double min;

        while(dijkstra.checkTour(dijkstra.getSmallestRoute(start), ende, start)){
            tour = dijkstra.getTour();
            min = getGamma(graph, tour);
            maxFlow += min;

            setFlow(graph, tour, min);

            graph = getResidualGraph(graph, tour);

            dijkstra.setVertices(graph.vertices);
            tour.clear();
        }

        return maxFlow;
    }

    /**
     *
     * @param graph - actual graph with modified Flow values
     * @param tour - minimal tour from start to end vertex
     * @return - modified graph (delete or add edges)
     */
    public Graph getResidualGraph(Graph graph, List<Vertex> tour){
        Edge temp;
        for(int i=0; i<tour.size()-1; i++){
            int src = tour.get(i).getData();
            int dest = tour.get(i + 1).getData();

            Edge edge = graph.vertices.get(src).getEdge(src, dest);

            if(edge != null){ // Für Hinwege
                if(edge.getFlow() == edge.getCapacity()){
                    // Falls kein Rückweg besteht muss dieser hinzugefügt und Hinweg entfernt werden da Flusswert = Kapazität
                    if(graph.vertices.get(dest).getEdge(dest, src) == null){
                        temp = new Edge(edge.getDest(), edge.getSrc(), edge.getCapacity());
                        graph.vertices.get(dest).addEdge(temp);
                        graph.vertices.get(src).removeEdge(edge);
                    }else{
                        graph.vertices.get(src).removeEdge(edge);
                    }
                }else if(edge.getFlow() != 0){
                    // Falls kein Rückweg besteht muss dieser hinzugefügt werden da Flusswert != 0
                    if(graph.vertices.get(dest).getEdge(dest, src) == null){
                        temp = new Edge(edge.getDest(), edge.getSrc(), edge.getCapacity());
                        temp.setFlow(edge.getCapacity() - edge.getFlow());
                        graph.vertices.get(dest).addEdge(temp);
                    }
                }else if(edge.getFlow() == 0){
                    // Falls ein Rückweg besteht muss dieser entfernt werden da Flusswert = 0
                    if(graph.vertices.get(dest).getEdge(dest, src) != null){
                        graph.vertices.get(dest).removeEdge(graph.vertices.get(dest).getEdge(dest, src));
                    }
                }
            }else { // Für Rückwege falls kein Hinweg besteht
                edge = graph.vertices.get(dest).getEdge(dest, src);
                if(edge.getFlow() == edge.getCapacity()){
                    // Hinweg muss hinzugefügt und Rückweg entfernt werden
                    temp = new Edge(edge.getSrc(), edge.getDest(), edge.getCapacity());
                    graph.vertices.get(src).addEdge(temp);
                    graph.vertices.get(dest).removeEdge(edge);

                }else if(edge.getFlow() != 0){
                    // Falls kein Hinweg besteht muss dieser hinzugefügt werden da Flusswert != 0
                    temp = new Edge(edge.getSrc(), edge.getDest(), edge.getCapacity());
                    temp.setFlow(edge.getCapacity() - edge.getFlow());
                    graph.vertices.get(src).addEdge(temp);
                }
                // Flow Wert = 0 entfällt da hier abgefangen wird für Rückwege, da Hinweg nicht existiert
            }

        }
        return graph;
    }

    /**
     *
     * @param graph - modified graph after getting the minimal
     * @param tour - minimal tour to set only the vertices flows in the tour
     * @param min - min value
     */
    public void setFlow(Graph graph, List<Vertex> tour, double min){
        for (int i=0; i<tour.size()-1; i++){
            int src = tour.get(i).getData();
            int dest = tour.get(i + 1).getData();

            if(vertices.get(src).getEdge(src, dest) != null){
                graph.vertices.get(src).getEdge(src, dest).setFlow(min);
                if(graph.vertices.get(dest).getEdge(dest, src) != null){
                    graph.vertices.get(dest).getEdge(dest, src).setFlow(-min);
                }
            }else {
                graph.vertices.get(dest).getEdge(dest, src).setFlow(-min);
                graph.vertices.get(src).getEdge(src, dest).setFlow(min);
            }

        }
    }

    /**
     *
     * @param graph - modified graph
     * @param tour - minimal tour
     * @return - minimal value of the minimal tour in the graph
     */
    public double getGamma(Graph graph, List<Vertex> tour){
        int src, dest;
        double min = Double.MAX_VALUE;
        Edge edge;

        for(int i=0; i<tour.size()-1; i++) {
            src = tour.get(i).getData();
            dest = tour.get(i + 1).getData();

            edge = graph.vertices.get(src).getEdge(src, dest);

            if(edge.getCapacity() - edge.getFlow() < min){
                min = edge.getCapacity() - edge.getFlow();
            }
        }

        return min;
    }

    public void setEdges(List<Vertex> vertexList) {
        List<Edge> edgeList = getEdges(vertices);

        for (Vertex v:vertexList
                ) {
            for (Edge edge : edgeList
                    ) {
                if (edge.getSrc() == v.getData()) {
                    v.addEdge(edge);
                }
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


}
