package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GraphCPM_UI {
    private GraphCPM graph;

    public GraphCPM_UI() {
        graph = new GraphCPM();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("CPM Graph Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // === Sekcja dodawania węzła ===
        JPanel nodePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        nodePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Dodaj węzeł",
                TitledBorder.LEFT, TitledBorder.TOP));

        JTextField nodeNameField = new JTextField();
        JTextField durationField = new JTextField();
        JButton addNodeButton = new JButton("Dodaj węzeł");

        nodePanel.add(new JLabel("Nazwa węzła:"));
        nodePanel.add(nodeNameField);
        nodePanel.add(new JLabel("Czas trwania:"));
        nodePanel.add(durationField);
        nodePanel.add(new JLabel());
        nodePanel.add(addNodeButton);

        // === Sekcja dodawania krawędzi ===
        JPanel edgePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        edgePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Dodaj połączenie",
                TitledBorder.LEFT, TitledBorder.TOP));

        JTextField edgeFromField = new JTextField();
        JTextField edgeToField = new JTextField();
        JButton addEdgeButton = new JButton("Dodaj krawędź");

        edgePanel.add(new JLabel("Z węzła:"));
        edgePanel.add(edgeFromField);
        edgePanel.add(new JLabel("Do węzła:"));
        edgePanel.add(edgeToField);
        edgePanel.add(new JLabel());
        edgePanel.add(addEdgeButton);

        // === Sekcja uruchomienia algorytmu ===
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton runCPMButton = new JButton("Uruchom CPM i pokaż graf");
        actionPanel.add(runCPMButton);

        // Dodanie paneli do głównego layoutu
        mainPanel.add(nodePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(edgePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(actionPanel);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        // === Obsługa akcji ===
        addNodeButton.addActionListener(e -> {
            String name = nodeNameField.getText().trim();
            try {
                double duration = Double.parseDouble(durationField.getText().trim());
                if (!name.isEmpty()) {
                    graph.addNode(name, duration);
                    JOptionPane.showMessageDialog(frame, "Dodano węzeł \"" + name + "\".");
                } else {
                    JOptionPane.showMessageDialog(frame, "Nazwa węzła nie może być pusta.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Czas trwania musi być liczbą.");
            }
        });

        addEdgeButton.addActionListener(e -> {
            String from = edgeFromField.getText().trim();
            String to = edgeToField.getText().trim();
            if (!from.isEmpty() && !to.isEmpty()) {
                try {
                    graph.addEdge(from, to);
                    JOptionPane.showMessageDialog(frame, "Dodano krawędź: " + from + " → " + to);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Niepoprawne dane krawędzi.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Podaj nazwy obu węzłów.");
            }
        });

        runCPMButton.addActionListener(e -> {
            try {
                graph.fixGraph();
                graph.StepForward("Start", 0.0);
                graph.StepBackward("Start");
                graph.CalculateReserve();
                graph.visualizeGraph();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas przetwarzania CPM:\n" + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        SwingUtilities.invokeLater(GraphCPM_UI::new);
    }
}
