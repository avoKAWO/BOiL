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

Zagadnienie transportowe (zadanie transportowe, problem transportowy, ang. transportation problem) – służy do obliczania najkorzystniejszego rozplanowania wielkości dostaw homogenicznego towaru pomiędzy dostawcami odbiorcami. W klasycznym ujęciu problem decyzyjny sformułowany jest jako zadanie programowania całkowitoliczbowego.

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

