package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZP {
    private int suppliers_amount;               // ilość dostawców (i)
    private int recipients_amount;              // ilość odbiorców (j)
    private double[] supply;                    // podaż
    private double[] unit_purchase_costs;       // jednostkowe koszty zakupu
    private double[] demands;                   // popyt
    private double[] unit_selling_price;        // ceny sprzedaży
    private double[][] unit_transport_costs;    // jednostkowe koszty transportu
    private double[][] unit_profits;            // Macierz zysków jednostkowych
    private double imagine_supply = 0;          // podaż fikcyjnego odbiorcy (OF)
    private double imagine_demand = 0;          // popyt fikcyjnego dostawcy (DF)
    private double[][] transport_plan;
    private double[] alfa;
    private double[] beta;

    public ZP(int suppliers_amount, int recipients_amount, double[] supply, double[] unit_purchase_costs, double[] demands, double[] unit_selling_price, double[][] unit_transport_costs) {
        this.suppliers_amount = suppliers_amount;
        this.recipients_amount = recipients_amount;
        if (supply.length != suppliers_amount)
            throw new IllegalArgumentException("Supply array length (" + supply.length + ") does not match suppliers_amount (" + suppliers_amount + ").");

        if (unit_purchase_costs.length != suppliers_amount)
            throw new IllegalArgumentException("Unit purchase costs array length (" + unit_purchase_costs.length + ") does not match suppliers_amount (" + suppliers_amount + ").");

        if (demands.length != recipients_amount)
            throw new IllegalArgumentException("Demands array length (" + demands.length + ") does not match recipients_amount (" + recipients_amount + ").");

        if (unit_selling_price.length != recipients_amount)
            throw new IllegalArgumentException("Unit selling price array length (" + unit_selling_price.length + ") does not match recipients_amount (" + recipients_amount + ").");

        if (unit_transport_costs.length != suppliers_amount)
            throw new IllegalArgumentException("Unit transport costs row count (" + unit_transport_costs.length + ") does not match suppliers_amount (" + suppliers_amount + ").");

        if (unit_transport_costs[0].length != recipients_amount)
            throw new IllegalArgumentException("Unit transport costs column count (" + unit_transport_costs[0].length + ") does not match recipients_amount (" + recipients_amount + ").");

        this.supply = supply;
        this.unit_purchase_costs = unit_purchase_costs;
        this.demands = demands;
        this.unit_selling_price = unit_selling_price;
        this.unit_transport_costs = unit_transport_costs;
        unit_profits = new double[suppliers_amount][recipients_amount];
        double all_supply = 0;
        for(double s : supply) all_supply += s;
        double all_demands = 0;
        for(double d : demands) all_demands += d;
        if(all_demands == all_supply)
        {
            transport_plan = new double[suppliers_amount][recipients_amount];
        }
        else
        {
            transport_plan = new double[suppliers_amount + 1][recipients_amount + 1];
            imagine_supply = all_demands;
            imagine_demand = all_supply;
        }
        alfa = new double[transport_plan.length];
        beta = new double[transport_plan[0].length];
        Arrays.fill(alfa, Double.NaN);
        Arrays.fill(beta, Double.NaN);
    }
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
    public void printUnitProfits() {
        System.out.println("Unit profits matrix:");
        for (double[] unitProfit : unit_profits) {
            for (double v : unitProfit) {
                System.out.printf("%10.2f", v);
            }
            System.out.println();
        }
    }
    static class ValueWithIndex {
        double value;
        int row;
        int col;

        ValueWithIndex(double value, int row, int col) {
            this.value = value;
            this.row = row;
            this.col = col;
        }
    }
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
    public void printTransportPlan() {
        System.out.println("Transport plan:");
        for (double[] doubles : transport_plan) {
            for (double aDouble : doubles) {
                System.out.printf("%10.2f", aDouble);
            }
            System.out.println();
        }
    }
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
    public void printAlfaAndBeta() {
        System.out.println("Alfa and Beta:");
        System.out.print("Alfa: ");
        for (double a : alfa) System.out.printf("%10.2f", a);
        System.out.print("\nBeta: ");
        for (double b : beta) System.out.printf("%10.2f", b);
        System.out.println();
    }
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
    public double calculateTotalCost() {
        double totalCost = 0;
        for (int i = 0; i < suppliers_amount; i++) {
            for (int j = 0; j < recipients_amount; j++) {
                totalCost += transport_plan[i][j] * (unit_purchase_costs[i] + unit_transport_costs[i][j]);
            }
        }
        return totalCost;
    }
    public double calculateIncome() {
        double income = 0;
        for (int i = 0; i < suppliers_amount; i++) {
            for (int j = 0; j < recipients_amount; j++) {
                income += transport_plan[i][j] * unit_selling_price[j];
            }
        }
        return income;
    }
    public double calculateProfit() {
        double profit = 0;
        for (int i = 0; i < suppliers_amount; i++) {
            for (int j = 0; j < recipients_amount; j++) {
                profit += transport_plan[i][j] * unit_profits[i][j];
            }
        }
        return profit;
    }

    public double[][] getUnitProfits() {return unit_profits; }
    public double[][] getTransportPlan() { return transport_plan; }
    public double[] getSupply() { return supply; }
    public double[] getUnitPurchaseCosts() { return unit_purchase_costs; }
    public double[] getDemands() { return demands; }
    public double[] getUnitSellingPrice() { return unit_selling_price; }
    public double[][] getUnitTransportCosts() { return unit_transport_costs; }
    public double[] getAlfa() { return alfa; }
    public double[] getBeta() { return beta; }

}
