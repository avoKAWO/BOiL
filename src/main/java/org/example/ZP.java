package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ZP {
    private int suppliers_amount;               // ilość dostawców (i)
    private int recipients_amount;              // ilość odbiorców (j)
    private double[] supply;                    // podaż
    private double[] unit_purchase_costs;       // jednostkowe koszty zakupu
    private double[] demands;                   // popyt
    private double[] unit_selling_price;                // ceny sprzedaży
    private double[][] unit_transport_costs;    // jednostkowe koszty transportu
    private double[][] unit_profits;            // Macierz zysków jednostkowych
    private double imagine_supply = 0;          // podaż fikcyjnego odbiorcy (OF)
    private double imagine_demand = 0;          // popyt fikcyjnego dostawcy (DF)
    private double all_supply = 0;
    private double all_demands = 0;
    private double[][] transport_plan;

    public ZP(int suppliers_amount, int recipients_amount, double[] supply, double[] unit_purchase_costs, double[] demands, double[] unit_selling_price, double[][] unit_transport_costs) {
        this.suppliers_amount = suppliers_amount;
        this.recipients_amount = recipients_amount;
        if (supply.length != suppliers_amount || unit_purchase_costs.length != suppliers_amount || demands.length != recipients_amount || unit_selling_price.length != recipients_amount || unit_transport_costs.length != suppliers_amount || unit_transport_costs[0].length != recipients_amount) throw new IllegalArgumentException();
        this.supply = supply;
        this.unit_purchase_costs = unit_purchase_costs;
        this.demands = demands;
        this.unit_selling_price = unit_selling_price;
        this.unit_transport_costs = unit_transport_costs;
        unit_profits = new double[suppliers_amount][recipients_amount];
        for(double s : supply) all_supply += s;
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
    }
    public void calculate_unit_profits() {
        for (int i = 0; i < unit_profits.length; i++) {
            for (int j = 0; j < unit_profits[i].length; j++) {
                if(i==suppliers_amount || j == recipients_amount) unit_profits[i][j] = 0;
                else {
                    unit_profits[i][j] = unit_selling_price[j]-unit_purchase_costs[i]-unit_transport_costs[i][j];
                }
            }
        }
    }
    public void print_unit_profits() {
        System.out.println("Unit profits matrix:");
        for (int i = 0; i < unit_profits.length; i++) {
            for (int j = 0; j < unit_profits[i].length; j++) {
                System.out.printf("%10.2f", unit_profits[i][j]);
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
    public void calculate_transport_plan() {
        int temp_i = 0;
        int temp_j = 0;
        double temp_value = unit_profits[0][0];
        double[] temp_supply = supply;
        double[] temp_demands = demands;
        double temp_imagine_supply = imagine_supply;
        double temp_imagine_demand = imagine_demand;
        double temp_all_supply = all_supply;
        double temp_all_demands = all_demands;

        List<ValueWithIndex> list = new ArrayList<>();
        for (int i = 0; i < unit_profits.length; i++) {
            for (int j = 0; j < unit_profits[i].length; j++) {
                list.add(new ValueWithIndex(unit_profits[i][j], i, j));
            }
        }
        Collections.sort(list, new Comparator<ValueWithIndex>() {
            public int compare(ValueWithIndex o1, ValueWithIndex o2) {
                return Double.compare(o2.value, o1.value);
            }
        });

        for (ValueWithIndex item : list)
        {
            double difference;
            if (temp_supply[item.row] > temp_demands[item.col]) difference = temp_demands[item.col];
            else difference = temp_supply[item.row];
            transport_plan[item.row][item.col] = difference;
            temp_supply[item.row]-=difference;
            temp_demands[item.col]-=difference;
        }
        for (int i = 0; i < transport_plan.length; i++) {
            if(i==transport_plan.length-1)
            {
                for (int j = 0; j < transport_plan[i].length-1; j++) {
                    double difference;
                    if (temp_imagine_supply > temp_demands[j]) difference = temp_demands[j];
                    else difference = temp_imagine_supply;
                    transport_plan[i][j] = difference;
                    temp_imagine_supply-=difference;
                    temp_demands[j]-=difference;
                }
                double difference;
                if (temp_imagine_supply > temp_imagine_demand) difference = temp_imagine_demand;
                else difference = temp_imagine_supply;
                transport_plan[i][transport_plan[i].length-1] = difference;
                temp_imagine_supply-=difference;
                temp_imagine_demand-=difference;
            }
            else {
                double difference;
                if (temp_supply[i] > temp_imagine_demand) difference = temp_imagine_demand;
                else difference = temp_supply[i];
                transport_plan[i][transport_plan[i].length-1] = difference;
                temp_supply[i]-=difference;
                temp_imagine_demand-=difference;
            }
        }
    }
    public void print_transport_plan() {
        System.out.println("Transport plan:");
        for (int i = 0; i < transport_plan.length; i++) {
            for (int j = 0; j < transport_plan[i].length; j++) {
                System.out.printf("%10.2f", transport_plan[i][j]);
            }
            System.out.println();
        }
    }
}
