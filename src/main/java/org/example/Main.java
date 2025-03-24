package org.example;
public class Main {
    public static void main(String[] args) {
        GraphCPM graph = new GraphCPM();
        graph.addNode( "A", 2.0);
        graph.addNode( "B", 5.0);
        graph.addNode( "C", 1.0);
        graph.addNode( "D", 6.0);
        graph.addNode( "E", 4.0);
        graph.addNode( "F", 2.0);
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "E");
        graph.addEdge("D", "E");
        graph.addEdge("D", "F");
        graph.fixGraph();
        graph.StepForward("Start", (double) 0);
        graph.StepBackward("Start");
        graph.printGraphEdges();
        System.out.print("\n");
        graph.printNodesData();
    }
}