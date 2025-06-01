# BOiL - Aplikacja: Graf CPM i Zagadnienie Pośrednika

Projekt zespołowy zrealizowany w ramach przedmiotu **Badania Operacyjne i Logistyka (BOiL)**.  
Celem aplikacji jest umożliwienie użytkownikowi rozwiązania dwóch klasycznych problemów z zakresu badań operacyjnych:  
- **Grafu CPM (Critical Path Method)**  
- **Zagadnienia Pośrednika** (problem transportowy z wieloma sprzedawcami i odbiorcami)

Użytkownik może wybrać, który z problemów chce rozwiązać, a następnie interaktywnie podać dane lub załadować je z pliku tekstowego. Aplikacja posiada graficzny interfejs użytkownika zbudowany w technologii Swing.

---

## 🧠 Funkcjonalności

### 🟦 1. Graf CPM (Critical Path Method)
- Wizualizacja grafu zależności między zadaniami
- Wyznaczanie ścieżki krytycznej
- Obliczanie parametrów czasowych dla każdego zadania:
  - Czas najwcześniejszego startu (ES)
  - Czas najpóźniejszego startu (LS)
  - Rezerwy czasowe
- Obsługa danych wejściowych:
  - Z poziomu GUI (Swing)
  - Z pliku `.txt`

### 🟩 2. Zagadnienie Pośrednika (problem transportowy)
- Obsługa **dowolnej liczby sprzedawców** i **dowolnej liczby odbiorców**
- Wczytywanie macierzy zysków
- Wyznaczanie zoptymalizowanej macierzy transportowej
- Obliczanie:
  - Całkowitego kosztu
  - Zysku
  - Przychodu
- Obsługa danych wejściowych:
  - Z poziomu GUI (Swing)
  - Z pliku `.txt`

---

## 🖥️ Technologie

- **Java 17+**
- **Swing** (interfejs graficzny)
- **Maven** (zarządzanie zależnościami i budowaniem projektu)

---

## ▶️ Uruchamianie aplikacji

1. **Zbuduj projekt:**
   ```bash
   mvn clean install
   ```

2. **Uruchom aplikację:**
   ```bash
   mvn exec:java -Dexec.mainClass="main.Main"
   ```

> Upewnij się, że masz zainstalowaną Javę w wersji 17 lub wyższej oraz Mavena.

---

## 📝 Dane wejściowe

### Przykład pliku tekstowego dla Grafu CPM
```
A,,5
B,,7
C,A,7
D,A,8
E,B,3
F,C,4
H,E,5
H,D,5
H,F,5
```

### Przykład pliku tekstowego dla Zagadnienia Pośrednika
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

---

## 👨‍💻 Zespół projektowy

Projekt został zrealizowany zespołowo przez studentów kierunku ___:

- Karol Woda
- Wojciech Zacharski
- Bartłomiej Warzecha

---

## 📚 Dokumentacja projektowa

Szczegółowe backlogi i harmonogramy prac znajdują się w folderze [`Documentation/`](./Documentation):

- [`backlogCPM.md`](./Documentation/backlogCPM.md)
- [`backlogZP.md`](./Documentation/backlogZP.md)

---

## 📄 Licencja

Projekt zrealizowany w celach edukacyjnych.  
Brak formalnej licencji open-source.
