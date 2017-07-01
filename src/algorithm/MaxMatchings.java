package algorithm;

import data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel on 01.07.2017.
 */
public class MaxMatchings {
    FordFulkerson fordFulkerson;
    private int V;
    private List<Vertex> vertices;
    private boolean gerichtet;
    private int matchings;

    public MaxMatchings(int V, List<Vertex> vertices, boolean gerichtet, int matchings) {
        this.V = V;
        this.vertices = vertices;
        this.gerichtet = gerichtet;
        this.matchings = matchings;
    }

    public double getMaxMatchings(){
        Vertex superQuelle = new Vertex(V);
        Vertex superSenke = new Vertex(V+1);

        //SetSuperQuelleandSenke
        setSuperQuelleandSenke(superQuelle, superSenke);

        //SetCapacity = 1
        setCapacity();

        //FordFulkerson/EdmondsKarp
        fordFulkerson = new FordFulkerson(V+2,vertices,gerichtet);

        return fordFulkerson.getMaxFlow(superQuelle.getData(), superSenke.getData());
    }

    public void setSuperQuelleandSenke(Vertex superQuelle, Vertex superSenke){
        Edge edge;
        for(int i=0;i < matchings; i++){
            edge = new Edge(V,i);
            superQuelle.addEdge(edge);
        }

        for(int i=matchings; i<V;i++){
            edge = new Edge(i,V+1);
            superSenke.addEdge(edge);
        }

        vertices.add(superQuelle);
        vertices.add(superSenke);
    }

    public void setCapacity(){
        for (Vertex vertex:vertices
                ) {
            for (Edge e:vertex.edges
                    ) {
                e.setCapacity(1);
            }
        }
    }
}
