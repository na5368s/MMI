package main;

import data.*;

import java.util.*;

public class Main {

	public static void main(String[] args) {

		ArrayList<String> textinput = new ArrayList<>();
		textinput.add("Fluss.txt");
		textinput.add("G_1_2.txt");

		boolean gerichtet = true;

		for (String string : textinput) {
			Graph graph = new Graph(gerichtet);

			graph.setFromEdgeList(string);
			System.out.println("Datei: " + string);

			long start_ms;

			System.out.println("-----------------------------------------------------");
			System.out.println("FordFulkerson:");
			System.out.println("-----------------------------------------------------");
			start_ms = System.currentTimeMillis();
			graph.fordFulkerson(0,7);
			System.out.println((System.currentTimeMillis() - start_ms) + " ms.");
			System.out.println();

		}

		/*ArrayList<String> textinput = new ArrayList<>();
		textinput.add("Wege1.txt");
		textinput.add("Wege2.txt");
		textinput.add("Wege3.txt");
		textinput.add("G_1_2.txt");

		boolean gerichtet = true;

		for (String string : textinput) {
			Graph graph = new Graph(gerichtet);

			graph.setFromEdgeList(string);
			System.out.println(string);



			long start_ms;

			if(string.equals("Wege1.txt")){
				System.out.println("-----------------------------------------------------");
				System.out.println("Dijkstra:");
				System.out.println("-----------------------------------------------------");
				start_ms = System.currentTimeMillis();
				graph.dijkstra(2,0);
				System.out.println((System.currentTimeMillis() - start_ms) + " ms.");
			}

			if(gerichtet){
				System.out.println("-----------------------------------------------------");
				System.out.println("Moore-Bellman-Ford:");
				System.out.println("-----------------------------------------------------");

				start_ms = System.currentTimeMillis();
				if(string.equals("G_1_2.txt")) {
					graph.moore_Bellman_Ford(0, 1);
				}else {
					graph.moore_Bellman_Ford(2, 0);
				}
				System.out.println((System.currentTimeMillis() - start_ms) + " ms.");
			}else{

				if(string.equals("G_1_2.txt")) {
					System.out.println("-----------------------------------------------------");
					System.out.println("Moore-Bellman-Ford:");
					System.out.println("-----------------------------------------------------");

					start_ms = System.currentTimeMillis();
					graph.moore_Bellman_Ford(0, 1);
					System.out.println((System.currentTimeMillis() - start_ms) + " ms.");
				}

			}


		}*/

	}

}
