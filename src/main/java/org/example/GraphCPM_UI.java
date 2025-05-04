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
        frame.setSize(600, 550);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        //Sekcja dodawania węzła
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

        //Sekcja dodawania krawędzi
        JPanel edgePanel = new JPanel();
        edgePanel.setLayout(new BoxLayout(edgePanel, BoxLayout.Y_AXIS));
        edgePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Dodaj połączenia",
                TitledBorder.LEFT, TitledBorder.TOP));

        JPanel singleEdgePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField edgeFromField = new JTextField();
        JTextField edgeToField = new JTextField();
        JButton addEdgeButton = new JButton("Dodaj krawędź");

        singleEdgePanel.add(new JLabel("Z węzła:"));
        singleEdgePanel.add(edgeFromField);
        singleEdgePanel.add(new JLabel("Do węzła:"));
        singleEdgePanel.add(edgeToField);
        singleEdgePanel.add(new JLabel());
        singleEdgePanel.add(addEdgeButton);

        JPanel multiEdgePanel = new JPanel(new BorderLayout(5, 5));
        JTextField multiEdgeField = new JTextField();
        JButton addMultiEdgeButton = new JButton("Dodaj wiele krawędzi");

        multiEdgePanel.add(new JLabel("Wiele krawędzi (np. A->B, B->C):"), BorderLayout.NORTH);
        multiEdgePanel.add(multiEdgeField, BorderLayout.CENTER);
        multiEdgePanel.add(addMultiEdgeButton, BorderLayout.EAST);

        edgePanel.add(singleEdgePanel);
        edgePanel.add(Box.createVerticalStrut(10));
        edgePanel.add(multiEdgePanel);

        //Sekcja uruchomienia algorytmu
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Odśwież graf");
        JButton runCPMButton = new JButton("Uruchom CPM");
        JButton loadFromFileButton = new JButton("Wczytaj z pliku");
        actionPanel.add(runCPMButton);
        actionPanel.add(refreshButton);
        actionPanel.add(loadFromFileButton);

        mainPanel.add(nodePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(edgePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(actionPanel);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        //Obsługa akcji
        addNodeButton.addActionListener(e -> {
            String name = nodeNameField.getText().trim();
            try {
                double duration = Double.parseDouble(durationField.getText().trim());
                if (!name.isEmpty()) {
                    graph.addNode(name, duration);
                    //graph.visualizeGraph();
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
                    //graph.visualizeGraph();
                    JOptionPane.showMessageDialog(frame, "Dodano krawędź: " + from + " → " + to);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Niepoprawne dane krawędzi.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Podaj nazwy obu węzłów.");
            }
        });

        addMultiEdgeButton.addActionListener(e -> {
            String text = multiEdgeField.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Pole nie może być puste.");
                return;
            }

            String[] pairs = text.split(",");
            int added = 0, failed = 0;

            for (String pair : pairs) {
                String[] nodes = pair.trim().split("->");
                if (nodes.length == 2) {
                    String from = nodes[0].trim();
                    String to = nodes[1].trim();
                    try {
                        graph.addEdge(from, to);
                        added++;
                    } catch (Exception ex) {
                        failed++;
                    }
                } else {
                    failed++;
                }
            }

            //graph.visualizeGraph();
            JOptionPane.showMessageDialog(frame,
                    "Dodano " + added + " krawędzi. Niepowodzenia: " + failed);
        });

        runCPMButton.addActionListener(e -> {
            try {
                graph.fixGraph();
                graph.StepForward("Start", 0.0);
                graph.StepBackward("Start");
                graph.CalculateReserve();
                graph.visualizeGraph();
                JOptionPane.showMessageDialog(frame, "CPM został przeliczony");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas przetwarzania CPM:\n" + ex.getMessage());
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                graph.fixGraph();
                //graph.StepForward("Start", 0.0);
                //graph.StepBackward("Start");
                graph.CalculateReserve();
                graph.visualizeGraph();
                JOptionPane.showMessageDialog(frame, "Graf odświeżony.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas odświeżania:\n" + ex.getMessage());
            }
        });
        loadFromFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                    int added = 0, failed = 0;

                    for (String line : lines) {
                        String[] parts = line.trim().split(",");
                        if (parts.length == 3) {
                            String name = parts[0].trim();
                            String predecessor = parts[1].trim();
                            String durationStr = parts[2].trim();
                            try {
                                double duration = Double.parseDouble(durationStr);
                                graph.addNode(name, duration);
                                if (!predecessor.isEmpty()) {
                                    graph.addEdge(predecessor, name);
                                }
                                added++;
                            } catch (Exception ex) {
                                failed++;
                            }
                        } else {
                            failed++;
                        }
                    }

                    graph.visualizeGraph();
                    JOptionPane.showMessageDialog(frame, "Wczytano " + added + " węzłów. Niepowodzenia: " + failed);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania pliku:\n" + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        SwingUtilities.invokeLater(GraphCPM_UI::new);
    }
}
