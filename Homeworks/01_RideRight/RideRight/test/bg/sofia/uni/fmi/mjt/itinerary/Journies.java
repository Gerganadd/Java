package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public class Journies {
    public static Journey VARNA_SOFIA_TRAIN = new Journey(VehicleType.TRAIN, Cities.VARNA, Cities.SOFIA, BigDecimal.valueOf(20));
    public static Journey VARNA_SOFIA_BUS = new Journey(VehicleType.BUS, Cities.VARNA, Cities.SOFIA, BigDecimal.valueOf(20));
    public static Journey VARNA_SOFIA_PLANE = new Journey(VehicleType.PLANE, Cities.VARNA, Cities.SOFIA, BigDecimal.valueOf(80));
    public static Journey VARNA_PLOVDIV_TRAIN = new Journey(VehicleType.TRAIN, Cities.VARNA, Cities.PLOVDIV, BigDecimal.valueOf(20));
    public static Journey VARNA_BURGAS_TRAIN = new Journey(VehicleType.TRAIN, Cities.VARNA, Cities.BURGAS, BigDecimal.valueOf(20));
    public static Journey DIMITOVGRAD_BURGAS_TRAIN = new Journey(VehicleType.TRAIN, Cities.DIMITROVGRAD, Cities.BURGAS, BigDecimal.valueOf(25));
}
