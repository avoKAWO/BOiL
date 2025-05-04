package org.example;

import java.util.*;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Edge;
import org.graphstream.ui.view.Viewer;

/**
 * Klasa odpowiedzialna za tworzenie grafu CPM i przeprowadzanie wszelkich potrzebnych operacji na nim
 */
public class GraphCPM {
    private Map<String, Node> nodes;
    private Graph graph;
    private Viewer viewer;

    public GraphCPM() {
        nodes = new HashMap<>();
        graph = null; // graf tworzony dopiero w visualizeGraph()
    }

    /**
     * Dodaje węzły do grafu
     *
     * @param name             nazwa węzła (nazwa zdarzenia)
     * @param activityDuration czas trwania zdarzenia
     */
    public void addNode(String name, Double activityDuration) {
        nodes.putIfAbsent(name, new Node(name, activityDuration));
    }

    /**
     * Tworzy połączenie skierowane pomiędzy węzłami
     *
     * @param from nazwa węzła(zdarzenia) z którego następuje połączenie
     * @param to   nazwa węzła(zdarzenia) do którego następuje połączenie
     * @apiNote Aby stworzyć połączenie obustronne należy powtórzyć wywołanie metody z odwrotną kolejnością wartości
     */
    public void addEdge(String from, String to) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) return;
        nodes.get(from).addChild(nodes.get(to));
    }

    /**
     * Tworzy węzły początku i końca grafu oraz odpowiednie połączenia
     */
    public void fixGraph() {
        if (!nodes.containsKey("Start")) nodes.put("Start", new Node("Start", 0.0));
        if (!nodes.containsKey("End")) nodes.put("End", new Node("End", 0.0));

        Node startNode = nodes.get("Start");
        Node endNode = nodes.get("End");

        Set<Node> nodesWithParents = new HashSet<>();
        Set<Node> allNodes = new HashSet<>(nodes.values());

        for (Node node : nodes.values()) {
            nodesWithParents.addAll(node.children);
        }

        for (Node node : allNodes) {
            if (!nodesWithParents.contains(node) && !node.getName().equals("Start") && !node.getName().equals("End")) {
                startNode.addChild(node);
            }
            if (node.children.isEmpty() && !node.getName().equals("Start") && !node.getName().equals("End")) {
                node.addChild(endNode);
            }
        }
    }

    /**
     * Przejście w przód — ES i EF
     *
     * @param startPoint   nazwa miejsca z którego rozpoczynamy przechodzenie - zazwyczaj "Start"
     * @param currentValue wartość która będzie oznaczała początkową wartość ES pierwszego węzła - zazwyczaj (double) 0
     */
    public void StepForward(String startPoint, Double currentValue) {
        Node currentNode = nodes.get(startPoint);
        if (currentNode == null) return;

        double earlyStart = Math.max(currentNode.getEarlyStartTime(), currentValue);
        currentNode.setEarlyStartTime(earlyStart);
        double earlyEnd = earlyStart + currentNode.getActivityDuration();
        currentNode.setEarlyEndTime(earlyEnd);

        for (Node child : currentNode.children) {
            StepForward(child.getName(), earlyEnd);
        }
    }

    /**
     * Przejście w tył — LS i LF
     *
     * @param startPoint nazwa miejsca z którego rozpoczynamy przechodzenie - zazwyczaj "Start"
     * @return zwraca rekurencyjną wartość poprzedniego węzła
     */
    public Double StepBackward(String startPoint) {
        Node currentNode = nodes.get(startPoint);
        Double currentValue = -1.0;
        if (currentNode == null) return null;

        for (Node child : currentNode.children) {
            Double temp = StepBackward(child.getName());
            if (currentValue == -1 || temp < currentValue) currentValue = temp;
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
     * Oblicza rezerwy i wykrywa niespójności
     *
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
     * Odświeża wizualizację grafu po przeliczeniu CPM
     */
    public void visualizeGraph() {
        if (graph == null) {
            graph = new SingleGraph("CPM Graph");
            setupStyle();
            viewer = graph.display();
        }

        // Dodaj brakujące węzły do GraphStream
        for (Node node : nodes.values()) {
            if (graph.getNode(node.getName()) == null) {
                org.graphstream.graph.Node gsNode = graph.addNode(node.getName());
                gsNode.setAttribute("ui.class", "noncritical");
            }

            String label = String.format(
                    "%s (%.1f)\n  ES: %.1f  EF: %.1f\n LS: %.1f  LF: %.1f\nR: %.1f",
                    node.getName(),
                    node.getActivityDuration(),
                    node.getEarlyStartTime(),
                    node.getEarlyEndTime(),
                    node.getLateStartTime(),
                    node.getLateEndTime(),
                    node.getReserve()
            );

            org.graphstream.graph.Node gsNode = graph.getNode(node.getName());
            gsNode.setAttribute("ui.label", label);

            if (Math.abs(node.getReserve()) < 1e-6) {
                gsNode.setAttribute("ui.class", "critical");
            } else {
                gsNode.setAttribute("ui.class", "noncritical");
            }
        }

        // Dodaj brakujące krawędzie
        for (Node parent : nodes.values()) {
            for (Node child : parent.children) {
                String edgeId = parent.getName() + "->" + child.getName();
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, parent.getName(), child.getName(), true);
                }

                boolean critical = Math.abs(parent.getReserve()) < 1e-6 &&
                        Math.abs(child.getReserve()) < 1e-6;

                graph.getEdge(edgeId).setAttribute("ui.class", critical ? "criticalEdge" : "defaultEdge");
            }
        }
    }

    /**
     * Stylizacja grafu
     */
    private void setupStyle() {
        graph.setAttribute("ui.stylesheet",
                "node {" +
                        "   text-size: 16px;" +
                        "   text-alignment: center;" +
                        "   fill-color: lightblue;" +
                        "   shape: box;" +
                        "   size-mode: fit;" +
                        "   padding: 10px;" +
                        "   stroke-mode: plain;" +
                        "   stroke-color: black;" +
                        "}" +
                        "node.critical {" +
                        "   fill-color: #ff4d4d;" +
                        "}" +
                        "node.noncritical {" +
                        "   fill-color: #87cefa;" +
                        "}" +
                        "edge.defaultEdge {" +
                        "   arrow-shape: arrow;" +
                        "   arrow-size: 10px, 6px;" +
                        "   fill-color: gray;" +
                        "   size: 2px;" +
                        "}" +
                        "edge.criticalEdge {" +
                        "   arrow-shape: arrow;" +
                        "   arrow-size: 10px, 6px;" +
                        "   fill-color: red;" +
                        "   size: 3px;" +
                        "}"
        );
    }
}
