package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
    public static ZP loadZPFromFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Pierwsza linia: liczby dostawców i odbiorców
            line = readNextDataLine(br);
            String[] parts = line.split(" ");
            int suppliersAmount = Integer.parseInt(parts[0]);
            int recipientsAmount = Integer.parseInt(parts[1]);

            // Kolejna linia: dostawy
            double[] supply = parseDoubleArray(readNextDataLine(br));

            // Kolejna linia: koszty zakupu jednostkowego
            double[] unitPurchaseCosts = parseDoubleArray(readNextDataLine(br));

            // Kolejna linia: zapotrzebowania
            double[] demands = parseDoubleArray(readNextDataLine(br));

            // Kolejna linia: koszty zakupu u odbiorców
            double[] purchaseCosts = parseDoubleArray(readNextDataLine(br));

            // Macierz kosztów transportu
            double[][] unitTransportCosts = new double[suppliersAmount][recipientsAmount];
            for (int i = 0; i < suppliersAmount; i++) {
                unitTransportCosts[i] = parseDoubleArray(readNextDataLine(br));
            }

            // Tworzymy obiekt ZP
            return new ZP(suppliersAmount, recipientsAmount, supply, unitPurchaseCosts,
                    demands, purchaseCosts, unitTransportCosts);
        }
    }

    // Pomocnicza metoda do parsowania tablicy double
    private static double[] parseDoubleArray(String line) {
        String[] parts = line.trim().split(" ");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i]);
        }
        return result;
    }

    // Pomocnicza metoda do czytania kolejnej niepustej linii niebędącej komentarzem
    private static String readNextDataLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#") && !line.startsWith("//")) {
                return line;
            }
        }
        throw new IOException("Unexpected end of file while reading data.");
    }
}
