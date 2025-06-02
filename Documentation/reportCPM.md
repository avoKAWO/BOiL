**Zespół 10:**  
Karol Woda  
Wojciech Zacharski  
Bartłomiej Warzecha  

**Przedmiot:** Badania Operacyjne i Logistyka  
**Data:** 05.05.25  
**Temat:** Metoda CPM  
**Kierunek:** WIMiIP ITE  
**Rok:** 3  
**Semestr:** 6  
**Grupa:** 4  

---

# METODA CPM

Metoda CPM (ang. Critical Path Method) to technika zarządzania projektami, która służy do planowania, monitorowania i kontrolowania realizacji projektów. Jej celem jest określenie najkrótszego czasu realizacji projektu przez identyfikację kluczowych działań oraz ścisłe powiązanie ich z innymi zadaniami.

## Kroki w metodzie CPM

1. **Określenie zadań** – Lista wszystkich działań, które muszą zostać zrealizowane w projekcie.  
2. **Określenie zależności między zadaniami** – Analiza, które zadania muszą zostać wykonane przed innymi, i w jakiej kolejności.  
3. **Obliczenie czasu trwania każdego zadania** – Określenie, ile czasu zajmie wykonanie poszczególnych działań.  
4. **Rysowanie wykresu sieciowego** – Tworzenie wizualnej reprezentacji projektu, z uwzględnieniem zależności i czasów realizacji.  
5. **Obliczenie ścieżki krytycznej** – Zidentyfikowanie najdłuższej drogi w projekcie, której opóźnienie wpłynie na opóźnienie całego projektu.  
6. **Analiza i zarządzanie** – Monitorowanie postępu prac oraz ewentualna korekta harmonogramu w zależności od przebiegu realizacji projektu.  

---

## Nasz projekt

Aplikacja okienkowa została zrealizowana w języku **Java**, działającym w środowisku JVM. Interfejs graficzny zaimplementowano z użyciem biblioteki **Swing**.

Projekt zarządzany był przez **Apache Maven**, który umożliwił m.in. definiowanie zależności w pliku `pom.xml`, automatyczne pobieranie ich z repozytoriów i wersjonowanie. Maven wspierał kompilację, pakowanie do `.jar`, integrację z zewnętrznymi narzędziami.

Środowisko pracy: **Java SE Development Kit (JDK)**  dostarczająca zarówno kompilator `javac`, jak i zestaw narzędzi developerskich oraz bibliotek klas standardowych. Całość została zorganizowana w klasyczną strukturę projektową Mavena (src/main/java, src/main/resources, src/test/java), co ułatwia integrację z zewnętrznymi systemami CI/CD oraz zapewnia zgodność z dobrymi praktykami projektowymi w ekosystemie Java.


---

<!-- ## Wizualizacja przykładowego zadania

**Plik .txt z danymi do programu**  
**Graf po wczytaniu z pliku .txt**  
**Graf po uruchomieniu CPM** -->

---

## Wybrane istotne fragmenty kodu

#### Wizualizacja grafu (GraphStream)
Utworzenie widoku grafu przy pomocy biblioteki GraphStream:

```java
public void visualizeGraph() {
   if (graph == null) {
       graph = new SingleGraph("CPM Graph");
       setupStyle();
       Layout layout = new SpringBox();
       graph.addSink(layout);
       Viewer viewer = graph.display();
   }
}
```

#### Dodawanie węzłów

```java
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
```

#### Dodawanie krawędzi

```java
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
```
### UI
#### Wczytanie informacji o grafie z pliku za pomocą przycisku:
```java
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
```

#### Obsługa uruchamiania metody CPM za pomocą przycisku:
```java
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
```


### Logika CPM

#### Klasa Node oraz jej konstruktor:
```java
public class Node {
   private String name;
   private Double activityDuration;
   List<Node> children;

   private Double reserve;
   private Double earlyStartTime;
   private Double earlyEndTime;
   private Double lateStartTime;
   private Double lateEndTime;

   public Node(String name, Double activityDuration) {
       this.name = name;
       this.activityDuration = activityDuration;
       this.children = new ArrayList<Node>();

       this.reserve = (double) 0;
       this.earlyStartTime = (double) 0;
       this.earlyEndTime = (double) 0;
       this.lateStartTime = (double) 0;
       this.lateEndTime = (double) 0;
   }
```

#### Przejście w przód po grafie:
```java
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
```

#### Przejście w tył po grafie:
```java
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
```

#### Obliczanie rezerwy:
```java
public void CalculateReserve() throws IllegalStateException {
   for (Node node : nodes.values()) {
       Double temp = node.getLateStartTime() - node.getEarlyStartTime();
       if (!temp.equals(node.getLateEndTime() - node.getEarlyEndTime())) {
           throw new IllegalStateException("Error: Reserve value mismatch for node: " + node);
       }
       node.setReserve(temp);
   }
}
```


