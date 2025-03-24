package org.example;

import java.util.*;

public class GraphCPM {
    private Map<String, Node> nodes;

    public GraphCPM() {
        nodes = new HashMap<>();
    }

    public void addNode(String name, Double activityDuration) {
        nodes.putIfAbsent(name, new Node(name, activityDuration));
    }

    public void addEdge(String from, String to) {
        nodes.get(from).addChild(nodes.get(to));
    }

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
    public Double StepBackward(String startPoint) {
        Node currentNode = nodes.get(startPoint);
        Double currentValue = 0.0;
        if (currentNode == null) return null;
        
        for (Node child : currentNode.children) {
            currentValue = StepBackward(child.getName());
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
    void printGraphEdges() {
        for (Node node : nodes.values()) {
            System.out.print(node.getName() + " -> ");
            for (Node child : node.children) {
                System.out.print(child.getName() + " ");
            }
            System.out.println();
        }
    }
    void printNodesData() {
        for (Node node : nodes.values()) {
            System.out.print(node.getName() + "("+node.getActivityDuration()+")" + ": ES = " + node.getEarlyStartTime() + "; EF = " + node.getEarlyEndTime() + "; LS = " + node.getLateStartTime() + "; LF = " + node.getLateEndTime()+"; R = " + node.getReserve()+ "\n");
        }
    }
}
