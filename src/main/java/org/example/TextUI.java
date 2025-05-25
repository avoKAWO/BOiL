package org.example;

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
        int suppliers_amount = 2, recipients_amount = 3;
        double[] supply = {20, 30};
        double[] unit_purchase_costs = {10, 12};
        double[] demands = {10, 28, 27};
        double[] purchase_costs = {30, 25, 30};
        double[][] unit_transport_costs = {{8, 14, 17}, {12, 9, 19}};
        ZP zp;
        try {
            zp = new ZP(suppliers_amount, recipients_amount, supply, unit_purchase_costs, demands, purchase_costs, unit_transport_costs);
        }
        catch (Exception e) {
            System.out.println("An error occurred while creating the ZP object: " + e.getMessage());
            return;
        }
        zp.calculateUnitProfits();
        zp.printUnitProfits();
        zp.calculateTransportPlan();
        zp.printTransportPlan();
        zp.calculateAlfaAndBeta();
        zp.printAlfaAndBeta();
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
