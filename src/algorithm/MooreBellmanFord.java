package algorithm;

import data.*;

import java.util.*;

/**
 * Created by Noel on 20.05.2017.
 */
public class MooreBellmanFord {
    private int V;
    private List<Vertex> vertices;
    private boolean gerichtet;
    private List<Edge> cycle;

    public MooreBellmanFord(int V, List<Vertex> vertices, boolean gerichtet) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
        cycle = new ArrayList<>();
    }

    /**
     *
     * @param start - starting vertex value
     * @return - graph with modifcated distance and predecessors for each vertex
     */
    public Graph getSmallestRoute(int start) {
        Graph graph = new Graph(gerichtet);
        Vertex v;
        for (int i = 0; i < V; i++) {
            v = new Vertex(i);
            v.setDistance(Double.POSITIVE_INFINITY);
            graph.vertices.add(v);
        }


        graph.vertices.get(start).setDistance(0);
        graph.vertices.get(start).setPrev(graph.vertices.get(start));


        setEdges(graph.vertices);
        List<Edge> edgeList = getEdges(graph.vertices);;
        List<Edge> tempListforCycle = new ArrayList<>();

        for(int i=0; i<vertices.size()-1; i++){
            for (Edge edge:edgeList
                 ) {
                if(graph.vertices.get(edge.getSrc()).getDistance() + edge.getWeight() < graph.vertices.get(edge.getDest()).getDistance()){
                    graph.vertices.get(edge.getDest()).setDistance(graph.vertices.get(edge.getSrc()).getDistance() + edge.getWeight());
                    graph.vertices.get(edge.getDest()).setPrev(graph.vertices.get(edge.getSrc()));
                }
            }

            edgeList = getEdges(graph.vertices);

            if(i==vertices.size()-2){
                if(checkCycle(edgeList, graph, tempListforCycle)){
                    edgeList = getEdges(graph.vertices);

                    checkCycle(edgeList, graph, tempListforCycle);

                    setCycle(tempListforCycle);
                }
            }
        }



        return graph;
    }

    public void printRouteandDistance(Graph graph, int ende, int start){
        if(start != ende){
            if(graph.vertices.get(ende).getPrev() != null) {
                rec(graph, graph.vertices.get(ende).getPrev().getData(), start);
                System.out.println(ende);
            }

        }else{
            System.out.println(ende);
        }

        System.out.println("Distance = " + graph.vertices.get(ende).getDistance());

    }

    // R�ckw�rts durchlaufen, da startwert = endwert am Anfang. Daher mit endwert r�ckw�rts laufen
    public void rec(Graph graph, int start, int ende){
        if(start != ende){
            if(graph.vertices.get(start).getPrev() != null) {
                rec(graph, graph.vertices.get(start).getPrev().getData(), ende);
                System.out.print(start + " ");
            }

        }else{
            System.out.print(ende + " ");
        }
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

    public boolean checkCycle(List<Edge> edgeList, Graph graph, List<Edge> tempListforCycle){
        boolean check = false;
        for (Edge edge:edgeList
                ) {
            if(graph.vertices.get(edge.getSrc()).getDistance() + edge.getWeight() < graph.vertices.get(edge.getDest()).getDistance()){
                graph.vertices.get(edge.getDest()).setDistance(graph.vertices.get(edge.getSrc()).getDistance() + edge.getWeight());
                graph.vertices.get(edge.getDest()).setPrev(graph.vertices.get(edge.getSrc()));
                check = true;
                tempListforCycle.add(edge);
            }
        }
        return check;
    }

    public void setCycle(List<Edge> edgeList){
        int start = edgeList.get(0).getSrc();
        boolean []visited = new boolean[V];

        while(true){
            for (Edge edge:edgeList
                 ) {
                if(!visited[edge.getDest()] && edge.getSrc() == start ){
                    cycle.add(edge);
                    start = edge.getDest();
                    visited[edge.getDest()] = true;
                }
            }

            if(cycle.get(0).getSrc() == cycle.get(cycle.size()-1).getDest()){
                break;
            }
        }
    }

    public List<Edge> getCycle(){
        return cycle;
    }

    public void printCycle(){
        System.out.println("Es existiert ein negativer Zykel: ");
        for (Edge edge:cycle
             ) {
            System.out.println(edge.getSrc() + " -> " + edge.getDest());
        }
    }

}
