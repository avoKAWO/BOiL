package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Interfejs graficzny do obsługi Zagadnienia Pośrednika (transport problem).
 * Pozwala na ręczne lub plikowe wprowadzenie danych, walidację oraz prezentację wyników.
 */
public class ZP_UI {
    // Główne okno aplikacji
    private JFrame inputFrame;
    // Panel z polami do wprowadzania danych
    private JPanel inputFieldsPanel;
    // Model zawierający dane problemu
    private ZP zp;

    // Pola tekstowe do kosztów transportu
    private JTextField[][] transportCostsFields;
    // Pola tekstowe do podaży
    private JTextField[] supplyFields;
    // Pola tekstowe do popytu
    private JTextField[] demandFields;
    // Pola tekstowe do kosztów zakupu jednostkowego
    private JTextField[] supplierUnitCostFields;
    // Pola tekstowe do cen sprzedaży jednostkowej
    private JTextField[] recipientUnitCostFields;

    /**
     * Konstruktor inicjuje okno główne.
     */
    public ZP_UI() {
        showInputFrame();
    }

    /**
     * Pomocnicza metoda do tworzenia pól tekstowych o tej samej szerokości.
     */
    private JTextField createTextField() {
        JTextField field = new JTextField(6);
        field.setPreferredSize(new Dimension(60, 24));
        return field;
    }

    /**
     * Tworzy i wyświetla główne okno do wprowadzania danych wejściowych.
     */
    private void showInputFrame() {
        inputFrame = new JFrame("Dane wejściowe");
        inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputFrame.setSize(900, 600);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setLayout(new BorderLayout());

        // Panel wyboru liczby dostawców i odbiorców oraz opcji ładowania z pliku
        JPanel selectionPanel = new JPanel(new FlowLayout());

        JSpinner supplierSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        JSpinner recipientSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        JButton confirmButton = new JButton("Potwierdź");

        JButton loadFromFileButton = new JButton("Załaduj z pliku");
        loadFromFileButton.addActionListener(e -> loadDataFromFile());

        selectionPanel.add(new JLabel("Liczba dostawców:"));
        selectionPanel.add(supplierSpinner);
        selectionPanel.add(new JLabel("Liczba odbiorców:"));
        selectionPanel.add(recipientSpinner);
        selectionPanel.add(confirmButton);
        selectionPanel.add(loadFromFileButton);

        // Panel na dynamicznie generowane pola wejściowe
        inputFieldsPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(inputFieldsPanel);

        // Obsługa przycisku potwierdzenia liczby dostawców i odbiorców
        confirmButton.addActionListener(e -> {
            int suppliersCount = (Integer) supplierSpinner.getValue();
            int recipientsCount = (Integer) recipientSpinner.getValue();
            zp = null; // reset modelu
            generateInputFields(suppliersCount, recipientsCount);
        });

        inputFrame.add(selectionPanel, BorderLayout.NORTH);
        inputFrame.add(scrollPane, BorderLayout.CENTER);
        inputFrame.setVisible(true);
    }

    /**
     * Generuje dynamicznie pola wejściowe do wprowadzania danych (popyt, podaż, koszty).
     * @param suppliersCount liczba dostawców
     * @param recipientsCount liczba odbiorców
     */
    private void generateInputFields(int suppliersCount, int recipientsCount) {
        inputFieldsPanel.removeAll();
        inputFieldsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        transportCostsFields = new JTextField[suppliersCount][recipientsCount];
        supplyFields = new JTextField[suppliersCount];
        demandFields = new JTextField[recipientsCount];
        supplierUnitCostFields = new JTextField[suppliersCount];
        recipientUnitCostFields = new JTextField[recipientsCount];

        int row = 0;

        // --- Pierwszy wiersz: etykiety kolumn ---
        gbc.gridy = row;
        gbc.gridx = 0;
        inputFieldsPanel.add(new JLabel(""), gbc); // pusty narożnik

        gbc.gridx = 1;
        inputFieldsPanel.add(new JLabel("Podaż", SwingConstants.CENTER), gbc);

        for (int j = 0; j < recipientsCount; j++) {
            gbc.gridx = j + 2;
            inputFieldsPanel.add(new JLabel("Odbiorca " + (j + 1), SwingConstants.CENTER), gbc);
        }
        gbc.gridx = recipientsCount + 2;
        inputFieldsPanel.add(new JLabel("Koszt zakupu", SwingConstants.CENTER), gbc);

        // --- Drugi wiersz: popyt ---
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        inputFieldsPanel.add(new JLabel("Popyt", SwingConstants.CENTER), gbc);
        gbc.gridx = 1;
        inputFieldsPanel.add(new JLabel(""), gbc);
        for (int j = 0; j < recipientsCount; j++) {
            gbc.gridx = j + 2;
            demandFields[j] = createTextField();
            inputFieldsPanel.add(demandFields[j], gbc);
        }
        gbc.gridx = recipientsCount + 2;
        inputFieldsPanel.add(new JLabel(""), gbc);

        // --- Wiersze dostawców ---
        for (int i = 0; i < suppliersCount; i++) {
            row++;
            gbc.gridy = row;

            // Etykieta dostawcy
            gbc.gridx = 0;
            inputFieldsPanel.add(new JLabel("Dostawca " + (i + 1), SwingConstants.CENTER), gbc);

            // Pole podaż
            gbc.gridx = 1;
            supplyFields[i] = createTextField();
            inputFieldsPanel.add(supplyFields[i], gbc);

            // Pola kosztów transportu
            for (int j = 0; j < recipientsCount; j++) {
                gbc.gridx = j + 2;
                transportCostsFields[i][j] = createTextField();
                transportCostsFields[i][j].setBackground(new Color(255, 114, 94)); // Pomarańczowe tło
                inputFieldsPanel.add(transportCostsFields[i][j], gbc);
            }

            // Pole kosztu zakupu
            gbc.gridx = recipientsCount + 2;
            supplierUnitCostFields[i] = createTextField();
            inputFieldsPanel.add(supplierUnitCostFields[i], gbc);
        }

        // --- Wiersz: cena sprzedaży ---
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        inputFieldsPanel.add(new JLabel("Cena sprzedaży", SwingConstants.CENTER), gbc);
        gbc.gridx = 1;
        inputFieldsPanel.add(new JLabel(""), gbc);
        for (int j = 0; j < recipientsCount; j++) {
            gbc.gridx = j + 2;
            recipientUnitCostFields[j] = createTextField();
            inputFieldsPanel.add(recipientUnitCostFields[j], gbc);
        }
        gbc.gridx = recipientsCount + 2;
        inputFieldsPanel.add(new JLabel(""), gbc);

        // --- Przycisk rozwiąż ---
        row++;
        gbc.gridy = row;
        gbc.gridx = recipientsCount + 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JButton calculateButton = new JButton("Rozwiąż");
        calculateButton.addActionListener(e -> runZPAndShowResult());
        inputFieldsPanel.add(calculateButton, gbc);

        // Pozwala zaakceptować Enterem
        inputFieldsPanel.getRootPane().setDefaultButton(calculateButton);

        inputFieldsPanel.revalidate();
        inputFieldsPanel.repaint();
    }

    /**
     * Generuje i wypełnia pola danymi z modelu (np. po wczytaniu pliku).
     */
    private void generateInputFields() {
        if (zp == null) return;
        int suppliersCount = zp.getSupply().length;
        int recipientsCount = zp.getDemands().length;
        generateInputFields(suppliersCount, recipientsCount);
        // Wypełnij pola danymi z modelu
        for (int i = 0; i < suppliersCount; i++) {
            supplyFields[i].setText(String.valueOf(zp.getSupply()[i]));
            supplierUnitCostFields[i].setText(String.valueOf(zp.getUnitPurchaseCosts()[i]));
            for (int j = 0; j < recipientsCount; j++) {
                transportCostsFields[i][j].setText(String.valueOf(zp.getUnitTransportCosts()[i][j]));
            }
        }
        for (int j = 0; j < recipientsCount; j++) {
            demandFields[j].setText(String.valueOf(zp.getDemands()[j]));
            recipientUnitCostFields[j].setText(String.valueOf(zp.getUnitSellingPrice()[j]));
        }
    }

    /**
     * Wczytuje dane problemu z pliku tekstowego.
     */
    private void loadDataFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        String userDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(userDir));
        fileChooser.setSelectedFile(new File(userDir + File.separator + "ZP_data.txt"));

        int res = fileChooser.showOpenDialog(inputFrame);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                zp = FileLoader.loadZPFromFile(file.getAbsolutePath());
                generateInputFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(inputFrame, "Błąd podczas ładowania pliku: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Odczytuje dane z pól wejściowych, waliduje i podświetla błędne pola.
     * Tworzy model ZP na podstawie poprawnych danych.
     */
    private void updateZPFromFields() {
        int suppliersCount = supplyFields.length;
        int recipientsCount = demandFields.length;

        double[] supply = new double[suppliersCount];
        double[] unitPurchaseCosts = new double[suppliersCount];
        double[] demands = new double[recipientsCount];
        double[] unitSellingPrices = new double[recipientsCount];
        double[][] transportCosts = new double[suppliersCount][recipientsCount];

        boolean hasError = false;
        Color errorColor = Color.PINK;
        Color okColor = Color.WHITE;
        Color transportOkColor = new Color(255, 114, 94);

        // Walidacja pól podaży
        for (int i = 0; i < suppliersCount; i++) {
            try {
                supply[i] = Double.parseDouble(supplyFields[i].getText().replace(",", "."));
                supplyFields[i].setBackground(okColor);
            } catch (NumberFormatException e) {
                supplyFields[i].setBackground(errorColor);
                hasError = true;
            }
        }
        // Walidacja kosztów zakupu
        for (int i = 0; i < suppliersCount; i++) {
            try {
                unitPurchaseCosts[i] = Double.parseDouble(supplierUnitCostFields[i].getText().replace(",", "."));
                supplierUnitCostFields[i].setBackground(okColor);
            } catch (NumberFormatException e) {
                supplierUnitCostFields[i].setBackground(errorColor);
                hasError = true;
            }
        }
        // Walidacja pól popytu
        for (int i = 0; i < recipientsCount; i++) {
            try {
                demands[i] = Double.parseDouble(demandFields[i].getText().replace(",", "."));
                demandFields[i].setBackground(okColor);
            } catch (NumberFormatException e) {
                demandFields[i].setBackground(errorColor);
                hasError = true;
            }
        }
        // Walidacja cen sprzedaży
        for (int i = 0; i < recipientsCount; i++) {
            try {
                unitSellingPrices[i] = Double.parseDouble(recipientUnitCostFields[i].getText().replace(",", "."));
                recipientUnitCostFields[i].setBackground(okColor);
            } catch (NumberFormatException e) {
                recipientUnitCostFields[i].setBackground(errorColor);
                hasError = true;
            }
        }
        // Walidacja kosztów transportu
        for (int i = 0; i < suppliersCount; i++) {
            for (int j = 0; j < recipientsCount; j++) {
                try {
                    transportCosts[i][j] = Double.parseDouble(transportCostsFields[i][j].getText().replace(",", "."));
                    transportCostsFields[i][j].setBackground(transportOkColor);
                } catch (NumberFormatException e) {
                    transportCostsFields[i][j].setBackground(errorColor);
                    hasError = true;
                }
            }
        }

        // Jeśli choć jedno pole błędne - nie twórz modelu, pokaż komunikat
        if (hasError) {
            JOptionPane.showMessageDialog(inputFrame,
                    "Podświetlono pola z błędnymi danymi – popraw je, aby kontynuować.",
                    "Błąd danych",
                    JOptionPane.ERROR_MESSAGE
            );
            throw new NumberFormatException("Błędne dane wejściowe.");
        }

        // Tworzenie modelu ZP jeśli brak błędów
        zp = new ZP(suppliersCount, recipientsCount, supply, unitPurchaseCosts, demands, unitSellingPrices, transportCosts);
    }

    /**
     * Uruchamia obliczenia na modelu ZP oraz prezentuje wyniki w nowym oknie.
     */

    private void runZPAndShowResult() {
        try {
            updateZPFromFields();
            // Obliczenia: zyski jednostkowe, plan transportowy, potencjały, optymalizacja
            zp.calculateUnitProfits();
            zp.calculateTransportPlan();
            zp.calculateAlfaAndBeta();
            zp.optimizeTransportPlan();

            double totalCost = zp.calculateTotalCost();
            double totalIncome = zp.calculateIncome();
            double totalProfit = zp.calculateProfit();
            double[] alfa = zp.getAlfa();
            double[] beta = zp.getBeta();

            // Budowa tekstowego wyniku
            StringBuilder result = new StringBuilder();
            result.append("Macierz zysków jednostkowych:\n");
            for (double[] row : zp.getUnitProfits()) {
                for (double val : row) {
                    result.append(String.format("%10.2f", val));
                }
                result.append("\n");
            }
            result.append("\nPlan transportowy:\n");
            for (double[] row : zp.getTransportPlan()) {
                for (double val : row) {
                    result.append(String.format("%10.2f", val));
                }
                result.append("\n");
            }

            // Dodaj wyświetlanie alfa i beta
            result.append("\nWektor alfa (potencjały dostawców):\n");
            for (double a : alfa) {
                result.append(String.format("%10.2f", a));
            }
            result.append("\nWektor beta (potencjały odbiorców):\n");
            for (double b : beta) {
                result.append(String.format("%10.2f", b));
            }

            result.append(String.format("\n\nKoszt całkowity: %.2f\n", totalCost));
            result.append(String.format("Przychód całkowity: %.2f\n", totalIncome));
            result.append(String.format("Zysk całkowity: %.2f\n", totalProfit));

            // Wyświetlenie wyników w osobnym oknie
            JTextArea outputArea = new JTextArea(result.toString());
            outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setPreferredSize(new Dimension(700, 400));
            JOptionPane.showMessageDialog(inputFrame, scrollPane, "Wyniki", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            // komunikat pokazała już updateZPFromFields()
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(inputFrame, "Błąd w danych wejściowych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Uruchomienie aplikacji.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ZP_UI::new);
    }
}