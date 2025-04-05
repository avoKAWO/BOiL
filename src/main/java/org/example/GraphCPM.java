package org.example;

import java.util.*;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Graph;

/**
 * Clasa odpowiedzialna za tworzenie grafu CPM i przeprowadzanie wszelkich potrzebnych operacji na nim
 */
public class GraphCPM {
    private Map<String, Node> nodes;

    public GraphCPM() {
        nodes = new HashMap<>();
    }

    /**
     * Dodaje węzły do grafu
     * @param name nazwa węzła (nazwa zdarzenia)
     * @param activityDuration czas trwania zdarzenia
     */
    public void addNode(String name, Double activityDuration) {
        nodes.putIfAbsent(name, new Node(name, activityDuration));
    }

    /**
     * Tworzy połączenie skierowane pomiędzy węzłami
     * @apiNote Aby stworzyć połączenie obustronne należy powtórzyć wywołanie metody z odwrotną kolejnością wartości
     * @param from nazwa węzła(zdarzenia) z którego następuje połączenie
     * @param to nazwa węzła(zdarzenia) do którego następuje połączenie
     */
    public void addEdge(String from, String to) {
        nodes.get(from).addChild(nodes.get(to));
    }

    /**
     * Tworzy węzły początku i końca grafu, a także dodaje odpowiednie połączenia
     */
    public void fixGraph() {
        Node startNode = new Node("Start", (double) 0);
        Node endNode = new Node("End", (double) 0);

        Set<Node> nodesWithParents = new HashSet<>();
        Set<Node> allNodes = new HashSet<>(nodes.values());

        for (Node node : nodes.values()) {
            nodesWithParents.addAll(node.children);
        }

        for (Node node : allNodes) {
            if (!nodesWithParents.contains(node)) {
                startNode.addChild(node);
            }
            if (node.children.isEmpty()) {
                node.addChild(endNode);
            }
        }

        nodes.put("Start", startNode);
        nodes.put("End", endNode);
    }

    /**
     * Metoda dokonująca przejścia w przód czego wynikiem są wartości ES i EF węzłów
     * Przykład użycia:
     * <code>
     *     graph.StepForward("Start", (double) 0);
     * </code>
     * @param startPoint nazwa miejsca z którego rozpoczynamy przechodzenie - zazwyczaj "Start"
     * @param currentValue  wartość która będzie oznaczała początkową wartość ES pierwszego węzła - zazwyczaj (double) 0
     */
    public void StepForward(String startPoint, Double currentValue) {
        Node currentNode = nodes.get(startPoint);
        if (currentNode == null) return;

        double earlyStart = Math.max(currentNode.getEarlyStartTime(), currentValue);
        currentNode.setEarlyStartTime(earlyStart);
        double earlyEnd = earlyStart + currentNode.getActivityDuration();
        currentNode.setEarlyEndTime(earlyEnd);

        currentValue = earlyEnd;

        for (Node child : currentNode.children) {
            StepForward(child.getName(), currentValue);
        }
    }

    /**
     * Metoda dokonująca przejścia w tył po grafie czego wynikiem są LS i LF
     * Przykład użycia:
     * <code>
     *     graph.StepBackward("Start");
     * </code>
     * @param startPoint nazwa miejsca z którego rozpoczynamy przechodzenie - zazwyczaj "Start"
     * @return zwraca rekurencyjną wartość poprzedniego węzła
     */
    public Double StepBackward(String startPoint) {
        Node currentNode = nodes.get(startPoint);
        Double currentValue = 0.0;
        if (currentNode == null) return null;
        
        for (Node child : currentNode.children) {
            Double temp = StepBackward(child.getName());
            if(currentValue==0 || temp < currentValue) currentValue = temp;
        }

        if (Objects.equals(startPoint, "End")) {
            currentValue = currentNode.getEarlyEndTime();
            currentNode.setLateEndTime(currentValue);
        }

        if (currentNode.getLateEndTime() == 0.0) {
            currentNode.setLateEndTime(currentValue);
        } else {
            currentNode.setLateEndTime(Math.min(currentNode.getLateEndTime(), currentValue));
        }

        double lateStart = currentNode.getLateEndTime() - currentNode.getActivityDuration();
        currentNode.setLateStartTime(lateStart);

        return lateStart;
    }

    /**
     * Metoda wyliczająca wartość rezerwy dla węzłów grafu
     * @throws IllegalStateException błąd gdy LS-ES != LF-EF
     */
    public void CalculateReserve() throws IllegalStateException {
        for (Node node : nodes.values()) {
            Double temp = node.getLateStartTime() - node.getEarlyStartTime();
            if (!temp.equals(node.getLateEndTime() - node.getEarlyEndTime())) {
                throw new IllegalStateException("Error: Reserve value mismatch for node: " + node);
            }
            node.setReserve(temp);
        }
    }

    /**
     * Metoda wypisująca w Terminalu wszystkie połączenia w grafie
     */
    void printGraphEdges() {
        for (Node node : nodes.values()) {
            System.out.print(node.getName() + " -> ");
            for (Node child : node.children) {
                System.out.print(child.getName() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Metoda wypisująca wszystkie dane węzłów grafu
     */
    void printNodesData() {
        for (Node node : nodes.values()) {
            System.out.print(node.getName() + "("+node.getActivityDuration()+")" + ": ES = " + node.getEarlyStartTime() + "; EF = " + node.getEarlyEndTime() + "; LS = " + node.getLateStartTime() + "; LF = " + node.getLateEndTime()+"; R = " + node.getReserve()+ "\n");
        }
    }

    /**
     * Testowa wizualizacja grafu
     */
    public void visualizeGraph() {
        Graph graph = new SingleGraph("CPM Graph");

        // Dodaj węzły
        for (Node node : nodes.values()) {
            String label = node.getName() + "\n" +
                    "ES:" + node.getEarlyStartTime() + "\n" +
                    "EF:" + node.getEarlyEndTime() + "\n" +
                    "LS:" + node.getLateStartTime() + "\n" +
                    "LF:" + node.getLateEndTime() + "\n" +
                    "R:" + node.getReserve();

            org.graphstream.graph.Node gsNode = graph.addNode(node.getName());
            gsNode.setAttribute("ui.label", label);

            if (node.getReserve() == 0) {
                gsNode.setAttribute("ui.class", "critical");
            }
        }

        // Dodaj krawędzie
        for (Node parent : nodes.values()) {
            for (Node child : parent.children) {
                String edgeId = parent.getName() + "->" + child.getName();
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, parent.getName(), child.getName(), true);
                }
            }
        }

        // Styl
        graph.setAttribute("ui.stylesheet",
                "node {" +
                        "   text-size: 14px;" +
                        "   fill-color: lightblue;" +
                        "   shape: box;" +
                        "}" +
                        "node.critical {" +
                        "   fill-color: red;" +
                        "}" +
                        "edge {" +
                        "   arrow-shape: arrow;" +
                        "   arrow-size: 10px, 5px;" +
                        "   fill-color: gray;" +
                        "}");

        graph.display();
    }

}
