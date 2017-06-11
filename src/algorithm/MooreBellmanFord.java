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
    private List<Vertex> cycle;

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
     * ---------------------------------------------------------------------------
     * Modified method getSmallesRoute for Praktikum with minCostFlow
     * Replace getCost() with getWeight()
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

        for(int i=0; i<vertices.size()-1; i++){
            for (Edge edge:edgeList
                 ) {
                if(graph.vertices.get(edge.getSrc()).getDistance() + edge.getCost() < graph.vertices.get(edge.getDest()).getDistance()){
                    graph.vertices.get(edge.getDest()).setDistance(graph.vertices.get(edge.getSrc()).getDistance() + edge.getCost());
                    graph.vertices.get(edge.getDest()).setPrev(graph.vertices.get(edge.getSrc()));
                }
            }

            edgeList = getEdges(graph.vertices);

            if(i==vertices.size()-2){
                if(checkCycle(edgeList, graph)){
                    Vertex startvertex;
                    List<Vertex> list = new ArrayList<>();
                    int j=0;
                    while(true){
                        if(graph.vertices.get(j).getPrev() != null){
                            if(list.contains(graph.vertices.get(j).getPrev())){
                                startvertex = graph.vertices.get(j).getPrev();
                                break;
                            }else{
                                list.add(graph.vertices.get(j).getPrev());
                                j = graph.vertices.get(j).getPrev().getData();
                            }
                        }else {
                            j++;
                        }
                    }
                    /*for(int j=0; j<graph.vertices.size()-1; j++){
                        if(list.contains(graph.vertices.get(j).getPrev())){
                            startvertex = graph.vertices.get(j).getPrev();
                        }else{
                            list.add(graph.vertices.get(j).getPrev());
                        }
                        //startvertex = graph.vertices.get(j).getPrev();
                    }*/
                    Vertex temp = startvertex;
                    cycle.add(temp);
                    temp = startvertex.getPrev();
                    while(graph.vertices.get(temp.getData()) != startvertex){
                        cycle.add(temp);
                        temp = graph.vertices.get(temp.getData()).getPrev();
                    }
                    cycle.add(graph.vertices.get(temp.getData()));
                    /*edgeList = getEdges(graph.vertices);

                    checkCycle(edgeList, graph);

                    setCycle(tempListforCycle);*/
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

    public boolean checkCycle(List<Edge> edgeList, Graph graph){
        boolean check = false;
        for (Edge edge:edgeList
                ) {
            if(graph.vertices.get(edge.getSrc()).getDistance() + edge.getCost() < graph.vertices.get(edge.getDest()).getDistance()){
                return true;
                /*graph.vertices.get(edge.getDest()).setDistance(graph.vertices.get(edge.getSrc()).getDistance() + edge.getCost());
                graph.vertices.get(edge.getDest()).setPrev(graph.vertices.get(edge.getSrc()));
                check = true;*/
                //tempListforCycle.add(edge);
            }
        }
        return check;
    }

    public List<Vertex> getCycle(){
        return cycle;
    }

    public void printCycle(){
        System.out.println("Es existiert ein negativer Zykel: ");
        for (Vertex vertex:cycle
             ) {
            System.out.print(vertex.getData() + " ");
        }
    }

}
