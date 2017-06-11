package algorithm;

import data.*;

import java.util.*;

/**
 * Created by Noel on 11.06.2017.
 */
public class CycleCanceling {
    private int V;
    private List<Vertex> vertices;
    private boolean gerichtet;

    public CycleCanceling(int V, List<Vertex> vertices, boolean gerichtet) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
    }

    public double getMinCostFlow(){
        boolean checkflow = setbFluss();

        if(checkflow) {
            while (true) {
                Graph graph = getResidualGraph();

                MooreBellmanFord mooreBellmanFord = new MooreBellmanFord(V, graph.vertices, gerichtet);
                double min;
                boolean check = false;
                for (Vertex vertex : graph.vertices
                        ) {
                    mooreBellmanFord.getSmallestRoute(vertex.getData());
                    if (mooreBellmanFord.getCycle().isEmpty()) {
                        continue;
                    } else {
                        check = true;
                        min = getGamma(mooreBellmanFord.getCycle(), graph);
                        setFlow(min, mooreBellmanFord.getCycle());
                        //mooreBellmanFord.printCycle();
                        break;
                    }
                }

                if (!check) {
                    break;
                }
            }

            double cost = 0;
            for (Vertex vertex : vertices
                    ) {
                for (Edge edge : vertex.edges
                        ) {
                    cost += (edge.getFlow() * edge.getCost());
                }
            }

            return cost;
        }
        return 0;
    }

    public boolean setbFluss(){
        List<Vertex> quelle = new ArrayList();
        List<Vertex> senke = new ArrayList();

        for (Vertex v : vertices
                ) {
            if (v.isSink()) {
                senke.add(v);
            } else {
                quelle.add(v);
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(V, vertices,gerichtet);
        for (Vertex vertexquelle:quelle
                ) {
            if(vertexquelle.getBalance() != 0) {
                for (Vertex vertexsenke : senke
                        ) {
                    if (vertexsenke.getBalance() != 0) {
                        fordFulkerson.getMaxFlow(vertexquelle.getData(), vertexsenke.getData());
                    }
                }
            }
        }

        for (Vertex vertex:senke
                ) {
            if(vertex.getBalance() < 0){
                System.out.println("Kein b-Fluss enthalten.");
                return false;
            }
        }
        return true;
    }

    public void setFlow(double min, List<Vertex> cycle){
        for(int i=cycle.size()-1; i>0; i--){
            int src = cycle.get(i).getData();
            int dest = cycle.get(i-1).getData();

            if(vertices.get(src).getEdge(src,dest) != null){
                vertices.get(src).getEdge(src, dest).setFlow(min);
            }else{
                vertices.get(dest).getEdge(dest,src).setFlow(-min);
            }
        }
    }

    public double getGamma(List<Vertex> cycle, Graph graph){
        double min = Double.MAX_VALUE;
        for(int i=cycle.size()-1; i>0; i--){
            int src = cycle.get(i).getData();
            int dest = cycle.get(i-1).getData();

            if(graph.vertices.get(src).getEdge(src,dest).getFlow() < min){
                min = graph.vertices.get(src).getEdge(src, dest).getFlow();
            }
        }

        return min;
    }

    public Graph getResidualGraph(){
        Graph graph = new Graph(gerichtet);
        Vertex v;

        for (int i = 0; i < V; i++) {
            v = new Vertex(i);
            graph.vertices.add(v);
        }

        for (Vertex vertex:vertices
             ) {
            for (Edge edge:vertex.edges
                 ) {
                if(edge.getCapacity() != edge.getFlow() && edge.getFlow() != 0){
                    Edge e1 = new Edge(edge.getSrc(), edge.getDest(),edge.getCapacity(), edge.getCost());
                    e1.setFlow(edge.getCapacity()-edge.getFlow());
                    Edge e2 = new Edge(edge.getDest(), edge.getSrc(), edge.getCapacity(), -(edge.getCost()));
                    e2.setFlow(edge.getFlow());

                    graph.vertices.get(edge.getSrc()).addEdge(e1);
                    graph.vertices.get(edge.getDest()).addEdge(e2);
                }else if(edge.getCapacity() == edge.getFlow()){
                    Edge e2 = new Edge(edge.getDest(), edge.getSrc(), edge.getCapacity(), -(edge.getCost()));
                    e2.setFlow(edge.getFlow());

                    graph.vertices.get(edge.getDest()).addEdge(e2);
                }else if(edge.getFlow() == 0){
                    Edge e1 = new Edge(edge.getSrc(), edge.getDest(),edge.getCapacity(), edge.getCost());
                    e1.setFlow(edge.getCapacity()-edge.getFlow());

                    graph.vertices.get(edge.getSrc()).addEdge(e1);
                }
            }
        }
        return graph;
    }
}
