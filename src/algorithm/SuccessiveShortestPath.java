package algorithm;

import data.*;

import java.util.*;

/**
 * Created by Noel on 16.06.2017.
 */
public class SuccessiveShortestPath {
    private int V;
    private List<Vertex> vertices;
    private boolean gerichtet;
    private List<Vertex> quelle, senke;

    public SuccessiveShortestPath(int V, List<Vertex> vertices, boolean gerichtet) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
        quelle = new ArrayList<>();
        senke = new ArrayList<>();
    }

    /**
     * set b'(v) and
     * check if there is a b-Fluss. If it exists return the minimum cost for flow
     * @return minimum cost for Flow
     */
    public double getMinCostFlow(){
        boolean check;
        Vertex svertex = null, tvertex = null;
        setBalanceTemp();
        do {
            quelle.clear();
            senke.clear();
            setQuelleAndSenke();

            Graph graph = getResidualGraph();
            Graph gtemp = null;

            MooreBellmanFord mooreBellmanFord = new MooreBellmanFord(V, graph.vertices, gerichtet);
            double min = 0;
            check = false;
            for (Vertex s : quelle
                    ) {
                gtemp = mooreBellmanFord.getSmallestRoute(s.getData());
                for (Vertex t : senke
                        ) {
                    if (gtemp.vertices.get(t.getData()).getPrev() != null) {
                        svertex = s;
                        tvertex = t;
                        check = true;
                        break;
                    }
                }

                if (check) {
                    break;
                }
            }

            if(!check){
                System.out.println("Es existiert kein b-Fluss.");
                break;
            }else {
                mooreBellmanFord.setTour(gtemp, tvertex.getData(), svertex.getData());
                List<Vertex> tour = mooreBellmanFord.getTour();
                double tourmin = getTourGamma(tour, gtemp);
                double smin = svertex.getBalance() - svertex.getBalancetemp();
                double tmin = tvertex.getBalancetemp() - tvertex.getBalance();
                min = getGamma(smin, tmin, tourmin);
                modifyBalanceandFlow(min, tour, svertex, tvertex);
            }
        }while(checkQuelleAndSenke());

        double cost = 0;

        if(check) {
            for (Vertex vertex : vertices
                    ) {
                for (Edge edge : vertex.edges
                        ) {
                    cost += edge.getFlow() * edge.getCost();
                }
            }
        }

        return cost;
    }

    /**
     * set balance of quelle and senke, set flow in original graph
     * @param min - Gamma to set balance and flow-value
     * @param tour - tour in ResidualGraph
     * @param svertex - starting vertex - quelle
     * @param tvertex - ending vertex - senke
     */
    public void modifyBalanceandFlow(double min, List<Vertex> tour, Vertex svertex, Vertex tvertex){
        vertices.get(svertex.getData()).setBalancetemp(min);
        vertices.get(tvertex.getData()).setBalancetemp(-min);

        for(int i=0; i<tour.size()-1; i++){
            int src = tour.get(i).getData();
            int dest = tour.get(i+1).getData();

            if(vertices.get(src).getEdge(src,dest) != null){
                vertices.get(src).getEdge(src,dest).setFlow(min);
            }else{
                vertices.get(dest).getEdge(dest,src).setFlow(-min);
            }
        }
    }

    /**
     * check if there is a quelle or senke after modifing the balances
     * @return - true if there is an option, false if not
     */
    public boolean checkQuelleAndSenke(){
        for (Vertex s:quelle
             ) {
            if(s.getBalance() != s.getBalancetemp()){
                return true;
            }
        }

        for (Vertex t:senke
             ) {
            if(t.getBalance() != t.getBalancetemp()){
                return true;
            }
        }

        return false;
    }

    /**
     * search for quelle and senke and set vertices to member variable "quelle" and "senke"
     */
    public void setQuelleAndSenke(){
        for (Vertex v : vertices
                ) {
            if(v.getBalance()-v.getBalancetemp() < 0){
                senke.add(v);
            }else if(v.getBalance()-v.getBalancetemp() > 0){
                quelle.add(v);
            }
        }
    }

    /**
     * set the balanceTemp - if there is an negative edge cost, modify balancetemp and set the flow value to value of capacity
     */
    public void setBalanceTemp(){

        for (Vertex vertex:vertices
             ) {
            for (Edge edge:vertex.edges
                 ) {
                if(edge.getCost() < 0){
                    vertex.setBalancetemp(edge.getCapacity());
                    vertices.get(edge.getDest()).setBalancetemp(-edge.getCapacity());
                    edge.setFlow(edge.getCapacity());
                }
            }
        }

    }

    /**
     * get the minimum between the min of quelle, senke and tour of edges flow
     * @param smin - minimum of quelle
     * @param tmin - minimum of senke
     * @param tourmin - minimum of edge flow in tour of ResidualGraph
     * @return minimum between the three options
     */
    public double getGamma(double smin, double tmin, double tourmin){
        double min = Double.MAX_VALUE;
        double balancemin;
        if(smin < tmin){
            balancemin = smin;
        }else{
            balancemin = tmin;
        }

        if(tourmin < balancemin){
            min = tourmin;
        }else{
            min = balancemin;
        }
        return min;
    }

    /**
     * get the minimum of edge flow in tour
     * @param tour - tour in Residualgraph
     * @param graph - ResidualGraph
     * @return minimum of edge flow in ResidualGraph in tour
     */
    public double getTourGamma(List<Vertex> tour, Graph graph){
        double min = Double.MAX_VALUE;
        for(int i=0; i<tour.size()-1; i++){
            int src = tour.get(i).getData();
            int dest = tour.get(i+1).getData();

            if(graph.vertices.get(src).getEdge(src,dest).getFlow() < min){
                min = graph.vertices.get(src).getEdge(src, dest).getFlow();
            }
        }

        return min;
    }

    /**
     * set ResidualGraph and return it
     * @return ResidualGraph
     */
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
