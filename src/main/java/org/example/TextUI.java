package org.example;

import java.io.IOException;
import java.util.Scanner;

public class TextUI {

    private static void TextUI_CPM() {
        System.setProperty("org.graphstream.ui", "swing");

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
        graph.CalculateReserve();
        System.out.print("\n");

        graph.visualizeGraph();
        System.out.println("The window with the CPM Graph has been opened...");
    }

    private static void TextUI_ZP() {

    }

    public static void main(String[] args) {
        boolean exitLoop = false;
        Scanner scanner = new Scanner(System.in);
        while(!exitLoop) {
            System.out.println("===== Actions =====");
            System.out.println("[1] CPM");
            System.out.println("[2] ZP");
            System.out.println("[-1] exit");
            System.out.println("===================");
            System.out.print("Choose action: ");
            int action = scanner.nextInt();
            System.out.println("===================");
            switch (action){
                case 1: {
                    TextUI_CPM();
                } break;
                case 2: {
                    TextUI_ZP();
                } break;
                case -1: {
                    exitLoop = true;
                } break;
                default: {
                    System.out.println("Invalid action");
                }
            }
            System.out.println();
        }
    }
}
