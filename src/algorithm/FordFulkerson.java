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
    private List<Integer> dfsroute;

    public FordFulkerson(int V, List<Vertex> vertices, boolean gerichtet) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
    }

    public double getMaxFlow(int start, int ende){
        Graph graph = new Graph(gerichtet);

        Vertex v;

        for (int i = 0; i < V; i++) {
            v = new Vertex(i);
            graph.vertices.add(v);
        }

        setEdges(graph.vertices);

        dijkstra = new Dijkstra(V, graph.vertices, gerichtet);
        dijkstra.setEdgesWeightEqual();
        List<Integer> tour = dijkstra.getTour(dijkstra.getSmallestRoute(start), ende, start);


        for (Integer a:tour
             ) {
            System.out.print(a + " ");
        }
        System.out.println();

        return 0;
    }

    public void recDepthSearchBefore(int start, int ende) {
        ArrayList<Vertex> queue = new ArrayList<>();
        dfsroute = new ArrayList<>();

        vertices.get(start).setVisited();
        queue.add(vertices.get(start));
        recDepthSearch(queue, ende);
        List<Integer> a = getDfsRoute();
        for (Integer b:a
             ) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    public boolean recDepthSearch(ArrayList<Vertex> queue, int ende) {
        boolean check = false;
        int index;
        while (!queue.isEmpty()) {
            Vertex temp = queue.remove(0);
            // System.out.print(temp.getData() + " ");
            dfsroute.add(temp.getData());
            if(temp.getData() == ende){
                check = true;
                return check;
            }
            for (Edge edge : temp.edges) {
                index = edge.getDest();
                if (!vertices.get(index).isVisited()) {
                    vertices.get(index).setVisited();
                    check = recDepthSearch(queue, ende);
                    if(check){
                        return check;
                    }else {
                        dfsroute.remove(dfsroute.size()-1);
                        queue.add(vertices.get(index));
                    }
                }
            }
        }
        return check;
    }

    public List<Integer> getDfsRoute(){
        return dfsroute;
    }

    public void iterativeBreadthSearch(Graph graph, int start, int ende) {
        ArrayList<Vertex> queue = new ArrayList<Vertex>();
        List<List<Integer>> array = new ArrayList<>();
        List<Integer> p = new ArrayList<>();

        graph.vertices.get(start).setVisited();
        queue.add(graph.vertices.get(start));
        p.add(start);

        int index;
        while (!queue.isEmpty()) {
            Vertex temp = queue.remove(0);
            for (Edge edge : temp.edges) {
                index = edge.getDest();
                if (!graph.vertices.get(index).isVisited()) {
                    graph.vertices.get(index).setVisited();
                    queue.add(graph.vertices.get(index));
                    p.add(index);
                }
            }
        }
        for (Integer i:p
                ) {
            System.out.print(i + " ");
        }
        System.out.println();
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
