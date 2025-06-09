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

Obliczanie zysków jednostkowych
```java
    public void calculateUnitProfits() {
        for (int i = 0; i < unit_profits.length; i++) {
            for (int j = 0; j < unit_profits[i].length; j++) {
                if(i==suppliers_amount || j == recipients_amount) unit_profits[i][j] = 0;
                else {
                    unit_profits[i][j] = unit_selling_price[j]-unit_purchase_costs[i]-unit_transport_costs[i][j];
                }
            }
        }
    }
```

Tworzenie planu transportu
```java
 public void calculateTransportPlan() {
        double[] temp_supply = supply.clone();
        double[] temp_demands = demands.clone();
        double temp_imagine_supply = imagine_supply;
        double temp_imagine_demand = imagine_demand;

        List<ValueWithIndex> list = new ArrayList<>();
        for (int i = 0; i < unit_profits.length; i++) {
            for (int j = 0; j < unit_profits[i].length; j++) {
                list.add(new ValueWithIndex(unit_profits[i][j], i, j));
            }
        }
        list.sort((o1, o2) -> Double.compare(o2.value, o1.value));

        for (ValueWithIndex item : list)
        {
            double difference = Math.min(temp_supply[item.row], temp_demands[item.col]);
            transport_plan[item.row][item.col] = difference;
            temp_supply[item.row]-=difference;
            temp_demands[item.col]-=difference;
        }
        for (int i = 0; i < transport_plan.length; i++) {
            if(i==transport_plan.length-1)
            {
                for (int j = 0; j < transport_plan[i].length-1; j++) {
                    double difference = Math.min(temp_imagine_supply, temp_demands[j]);
                    transport_plan[i][j] = difference;
                    temp_imagine_supply-=difference;
                    temp_demands[j]-=difference;
                }
                double difference = Math.min(temp_imagine_supply, temp_imagine_demand);
                transport_plan[i][transport_plan[i].length-1] = difference;
                temp_imagine_supply-=difference;
                temp_imagine_demand-=difference;
            }
            else {
                double difference = Math.min(temp_supply[i], temp_imagine_demand);
                transport_plan[i][transport_plan[i].length-1] = difference;
                temp_supply[i]-=difference;
                temp_imagine_demand-=difference;
            }
        }
    }
```

Obliczanie współczynników alfa i beta
```java
    public void calculateAlfaAndBeta(){
        alfa[alfa.length-1] = 0;

        boolean updated;
        do {
            updated = false;
            for (int i = 0; i < transport_plan.length; i++) {
                for (int j = 0; j < transport_plan[i].length; j++) {
                    if (transport_plan[i][j] > 0) {
                        if (!Double.isNaN(alfa[i]) && Double.isNaN(beta[j])) {
                            if (i < unit_profits.length && j < unit_profits[i].length) beta[j] = unit_profits[i][j] - alfa[i];
                            else if (alfa[i]!=0) beta[j] = -alfa[i];
                            else beta[j] = alfa[i];
                            updated = true;
                        } else if (Double.isNaN(alfa[i]) && !Double.isNaN(beta[j])) {
                            if (i < unit_profits.length && j < unit_profits[i].length) alfa[i] = unit_profits[i][j] - beta[j];
                            else  if (beta[i]!=0) alfa[i] = -beta[j];
                            else beta[j] = alfa[i];
                            updated = true;
                        }
                    }
                }
            }
        } while (updated);
    }
```
Sprawdzenie optymalności
```java
    private double[][] calculateDelta() {
        double[][] delta = new double[transport_plan.length][transport_plan[0].length];
        for (int i = 0; i < transport_plan.length; i++) {
            for (int j = 0; j < transport_plan[i].length; j++) {
                if (transport_plan[i][j] == 0) {
                    if (i < unit_profits.length && j < unit_profits[i].length) delta[i][j] = unit_profits[i][j] - alfa[i] - beta[j];
                    else delta[i][j] = - alfa[i] - beta[j];
                } else {
                    delta[i][j] = Double.NEGATIVE_INFINITY;
                }
            }
        }
        return delta;
    }
```

Optymalizacja planu dostaw
```java
    public void optimizeTransportPlan() {
        while (true) {
            calculateAlfaAndBeta();
            double[][] delta = calculateDelta();

            // Szukamy największego dodatniego delta
            double maxDelta = 0;
            int maxI = -1, maxJ = -1;
            for (int i = 0; i < delta.length; i++) {
                for (int j = 0; j < delta[i].length; j++) {
                    if (delta[i][j] > maxDelta) {
                        maxDelta = delta[i][j];
                        maxI = i;
                        maxJ = j;
                    }
                }
            }

            if (maxDelta <= 0) {
                System.out.println("Optimal plan — no positive criterion variables.");
                break;
            }

            // Jeśli jest dodatnie delta, tworzymy pętlę zamkniętą i modyfikujemy plan
            System.out.println("Optimization: largest delta = " + maxDelta + " for (" + maxI + "," + maxJ + ")");

            // Prosty algorytm: znajdź minimum z dostępnych w tej kolumnie i wierszu
            double minValue = Double.MAX_VALUE;
            int minI = -1, minJ = -1;

            // Szukamy minimum w wierszu maxI (pomijając maxJ)
            for (int j = 0; j < transport_plan[maxI].length; j++) {
                if (j != maxJ && transport_plan[maxI][j] > 0 && transport_plan[maxI][j] < minValue) {
                    minValue = transport_plan[maxI][j];
                    minI = maxI;
                    minJ = j;
                }
            }
            // Szukamy minimum w kolumnie maxJ (pomijając maxI)
            for (int i = 0; i < transport_plan.length; i++) {
                if (i != maxI && transport_plan[i][maxJ] > 0 && transport_plan[i][maxJ] < minValue) {
                    minValue = transport_plan[i][maxJ];
                    minI = i;
                    minJ = maxJ;
                }
            }

            if (minValue == Double.MAX_VALUE) {
                System.out.println("No possible loop for (" + maxI + "," + maxJ + ")");
                break;
            }

            // Zmieniamy plan: dodajemy minValue w maxI,maxJ a odejmujemy z minI,minJ
            transport_plan[maxI][maxJ] += minValue;
            transport_plan[minI][minJ] -= minValue;

            System.out.println("Added " + minValue + " to (" + maxI + "," + maxJ + "), subtracted from (" + minI + "," + minJ + ")");
        }
    }
```

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
Sprawdzenie poprawności danych wejściowych
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
