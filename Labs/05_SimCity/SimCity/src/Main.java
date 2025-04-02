import bg.sofia.uni.fmi.mjt.simcity.buildings.ResidentialBuilding;
import bg.sofia.uni.fmi.mjt.simcity.plot.Plot;
import bg.sofia.uni.fmi.mjt.simcity.utility.UtilityService;
import bg.sofia.uni.fmi.mjt.simcity.utility.UtilityType;

import java.util.HashMap;
import java.util.Map;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ResidentialBuilding rb = new ResidentialBuilding(200, 20, 20, 20);

        Plot pl = new Plot(2000);

        Map<UtilityType, Double> taxes = new HashMap<>();
        taxes.put(UtilityType.WATER, 20.0);
        taxes.put(UtilityType.NATURAL_GAS, 100.0);
        taxes.put(UtilityType.ELECTRICITY, 20.2);

        UtilityService us = new UtilityService(taxes);

        System.out.println(us.getTotalUtilityCosts(rb));
    }
}