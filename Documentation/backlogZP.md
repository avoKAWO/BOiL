Karol Woda\
Wojciech Zacharski\
Bartłomiej Warzecha

# Backlog Zagadnienie Pośrednika

## Stworzenie struktur danych
#### 📄 Opis:
Zdefiniowanie podstawowych klas/struktur danych (np. `Transport`, `Node`, `Route`, `Matrix`) z myślą o dalszym przetwarzaniu danych w aplikacji. Struktury muszą wspierać dynamiczne dodawanie i edycję danych wejściowych.
#### 🧩 Przypadki użycia:
Przyjmowanie danych z pliku, dodawanie nowych tras lub węzłów, aktualizacja zapotrzebowania/podaży.
#### ✔️ Kryterium akceptacji:
Zdefiniowane klasy umożliwiają odczyt, modyfikację i walidację danych bez błędów. Możliwość wczytania przykładowego zbioru danych bez potrzeby zmian w kodzie. Brak danych, lub błędne dane obsłużone.
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🟢 <span style="color: green"> **S**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **22.05.2025**

## Stworzenie macierzy zysków
#### 📄 Opis:
Implementacja funkcji generującej macierz zysków dla wszystkich możliwych tras między dostawcami a odbiorcami.
#### 🧩 Przypadki użycia:
Użytkownik wprowadza ceny jednostkowe – system generuje macierz `n x m` zysków.
#### ✔️ Kryterium akceptacji:
Dla każdego połączenia `(i,j)` poprawnie obliczony zysk `z[i][j]`.
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🟢 <span style="color: green"> **S**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **22.05.2025**

## Generowanie tablicy transportowej
#### 📄 Opis:
Utworzenie początkowej tablicy transportowej na podstawie bilansu podaży i popytu.
#### 🧩 Przypadki użycia:
Wstępna alokacja zasobów w celu rozpoczęcia optymalizacji. Uwzględnienie tzw. „sztucznego węzła”, gdy suma podaży ≠ suma popytu.
#### ✔️ Kryterium akceptacji:
Wygenerowana tabela zawiera kompletne dane i zachowuje warunek bilansu podaży i popytu. Obsługiwane są przypadki z nierównowagą.
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🟡 <span style="color: yellow"> **M**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **22.05.2025**

## Wyznaczanie współczynników alfa i beta
#### 📄 Opis:
Obliczanie potencjałów węzłów (`alfa` – dla sprzedawców, `beta` – dla odbiorców) niezbędnych do wyznaczenia `delty` przy sprawdzaniu optymalności.
#### 🧩 Przypadki użycia:
Używane w kolejnym kroku algorytmu – sprawdzeniu optymalności tras.
#### ✔️ Kryterium akceptacji:
Dla każdej przypisanej komórki `i`,`j`: `z[i][j]` = `alfa[i]` + `beta[j]`. Co najmniej jeden współczynnik ustalony z góry (np. `alfa[alfa.length-1] = 0`).
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🟢 <span style="color: green"> **S**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **25.05.2025**

## Mechanizm sprawdzania optymalności tras
#### 📄 Opis:
Obliczanie wskaźnika poprawności (`delta`) dla wszystkich nieprzypisanych komórek. Pozwala określić, czy obecny rozkład jest optymalny.
#### 🧩 Przypadki użycia:
Weryfikacja potrzeby dalszej optymalizacji.
#### ✔️ Kryterium akceptacji:
Poprawnie obliczone delty dla wszystkich nieprzypisanych pól. Jeśli wszystkie delty ≥ 0, system zgłasza stan optymalny.
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🟡 <span style="color: yellow"> **M**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **25.05.2025**

## Mechanizm optymalizacji rozkładu towaru na trasach
#### 📄 Opis:
Implementacja pełnego algorytmu optymalizacji metodą potencjałów lub metodą MODI.
#### 🧩 Przypadki użycia:
Wykonywany po sprawdzeniu nieoptymalności aktualnego rozkładu. Przesuwa ładunki zgodnie z wyznaczoną pętlą cykliczną.
#### ✔️ Kryterium akceptacji:
Po przeprowadzeniu optymalizacji zysk końcowy wzrasta lub pozostaje bez zmian (jeśli już optymalnie). Brak błędów przy zmianie alokacji.
#### Piorytet: 🔴 <span style="color: red"> **L**</span>
#### Trudność: 🔴 <span style="color: red"> **L**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **25.05.2025**

## Obliczanie zysku, przychódu i całkowitego kosztu
#### 📄 Opis:
Obliczenie kluczowych wartości finansowych wynikających z rozkładu towarów:
- Przychód – suma wartości jednostkowych za przewiezione towary.
- Koszt całkowity – suma kosztów transportu na przypisanych trasach.
- Zysk – różnica między przychodem a kosztem całkowitym.
#### 🧩 Przypadki użycia:
Użytkownik po zakończeniu optymalizacji powinien móc poznać zysk, przychód i całkowite koszty.
#### ✔️ Kryterium akceptacji:
Przychód, koszt i zysk obliczane są zgodnie z obowiązującą alokacją transportową. Dane są kompletne i poprawne dla przykładowych danych testowych. Wynik można łatwo wyeksportować lub przedstawić w postaci tabeli/raportu.
#### Piorytet: 🟡 <span style="color: yellow"> **M**</span>
#### Trudność: 🟢 <span style="color: green"> **S**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **25.05.2025**


## Wprowadzanie danych z pliku
#### 📄 Opis:
Import danych wejściowych z pliku `.txt` zawierających informacje o dostawcach, odbiorcach, zapotrzebowaniach i kosztach.
#### 🧩 Przypadki użycia:
Użytkownik importuje dane zamiast wprowadzać ręcznie.
#### ✔️ Kryterium akceptacji:
Po wczytaniu pliku system tworzy poprawną strukturę danych gotową do dalszych obliczeń. Walidacja danych i błędów formatu.
#### Piorytet: 🟢 <span style="color: green"> **S**</span>
#### Trudność: 🟡 <span style="color: yellow"> **M**</span>
#### 👤 Wykonawca: **Karol Woda**
#### 📅 Deadline: **25.05.2025**

## Interfejs Graficzny: przyjmowanie danych wejściowych
#### 📄 Opis:
W osobnym oknie niż wynik
#### 🧩 Przypadki użycia:
#### ✔️ Kryterium akceptacji:
#### Piorytet: 🟡 <span style="color: yellow"> **M**</span>
#### Trudność: 🔴 <span style="color: red"> **L**</span>
#### 👤 Wykonawca: **Bartłomiej Warzecha**
#### 📅 Deadline: ****

## Interfejs Graficzny: dynamiczne dodawanie dostawców i odbiorców
#### 📄 Opis:
Przy uruchamianiu programu - zapytanie o ilość, przyciski dodawania dostawców i odbiorców
#### 🧩 Przypadki użycia:

#### ✔️ Kryterium akceptacji:

#### Piorytet: 🟡 <span style="color: yellow"> **M**</span>
#### Trudność: 🔴 <span style="color: red"> **L**</span>
#### 👤 Wykonawca: **Bartłomiej Warzecha **
#### 📅 Deadline: ****

## Interfejs Graficzny: wyświetlanie wyników
#### 📄 Opis:
W osobnym oknie niż dane wejściowe
#### 🧩 Przypadki użycia:

#### ✔️ Kryterium akceptacji:

#### Piorytet: 🟡 <span style="color: yellow"> **M**</span>
#### Trudność: 🟡 <span style="color: yellow"> **M**</span>
#### 👤 Wykonawca: **Wojciech Zacharski**
#### 📅 Deadline: ****
