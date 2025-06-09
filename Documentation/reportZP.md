**Zespół 10:**  
Karol Woda  
Wojciech Zacharski  
Bartłomiej Warzecha  

**Przedmiot:** Badania Operacyjne i Logistyka  
**Data:** 09.06.25  
**Temat:** Zagadnienie pośrednika  
**Kierunek:** WIMiIP ITE  
**Rok:** 3  
**Semestr:** 6  
**Grupa:** 4  

---

# ZAGADNIENIE POŚREDNIKA

Zagadnienie transportowe (znane także jako zadanie transportowe lub problem transportowy, ang. transportation problem) dotyczy wyznaczenia optymalnego planu dystrybucji jednorodnego towaru pomiędzy dostawcami a odbiorcami. Celem jest takie rozlokowanie wielkości dostaw, aby zminimalizować całkowity koszt transportu przy jednoczesnym zaspokojeniu popytu i wykorzystaniu dostępnej podaży.

## Kroki w metodzie pośresdnika

1. Obliczenie zysku jednostkowego - Zysk pośrednika dla poszczególnych transakcji 
2. Utworzenie macierzy zysków - Macierz zawierająca zyski jednostkowe, podaż i popty każdego dostawcy/odbiorcy oraz fikcyjnych dostawców i odbiorców
3. Rozpisanie pierwszego optymalnego planu dostaw - Rozpisanie przewozów na trasach kierując się zasadą maksymalnego elementu macierzy
4. Sprawdzenie, czy rozwiązanie spełnia warunek funkcji celu - Obliczenie współczynników alfa i beta
5. Sprawdzenie optymalności planu - Obliczenie o ile wzrośnie zysk po przerzuceniu towaru na poszczególną trasę
6. Ewentualna optymalizacja planu - Ponowne rozpisanie planu dostaw uwzględniając informacje z punktu 5.
---

## Nasz projekt

Aplikacja okienkowa została zrealizowana w języku **Java**, działającym w środowisku JVM. Interfejs graficzny zaimplementowano z użyciem biblioteki **Swing**.

Projekt zarządzany był przez **Apache Maven**, który umożliwił m.in. definiowanie zależności w pliku `pom.xml`, automatyczne pobieranie ich z repozytoriów i wersjonowanie. Maven wspierał kompilację, pakowanie do `.jar`, integrację z zewnętrznymi narzędziami.

Środowisko pracy: **Java SE Development Kit (JDK)**  dostarczająca zarówno kompilator `javac`, jak i zestaw narzędzi developerskich oraz bibliotek klas standardowych. Całość została zorganizowana w klasyczną strukturę projektową Mavena (src/main/java, src/main/resources, src/test/java), co ułatwia integrację z zewnętrznymi systemami CI/CD oraz zapewnia zgodność z dobrymi praktykami projektowymi w ekosystemie Java.

---

## Wybrane istotne fragmenty kodu


### Logika 


### UI

Wczytywanie danych z pliku .txt
```java
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
```

Format pliku .txt
```
# liczba dostawców i odbiorców
2 3

# Podaż
20 30

# koszty zakupu jednostkowego
10 12

# Popyt
10 28 27

# koszty zakupu u odbiorców
30 25 30

# macierz kosztów transportu
8 14 17
12 9 19
```
Walidacja danych wejściowych
```java
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
```
Uruchomienie algorytmu i wyprowadzenie wyników
```java
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
```
Wyświetlanie wyników w osobnym oknie
```java
            if(resultFrame == null){
                resultFrame = new JFrame("Wyniki");
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                outputArea = new JTextArea();
                outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                outputArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(outputArea);
                scrollPane.setPreferredSize(new Dimension(700, 400));

                resultFrame.getContentPane().add(scrollPane);
                resultFrame.pack();
                resultFrame.setLocationRelativeTo(inputFrame);
            }
```
