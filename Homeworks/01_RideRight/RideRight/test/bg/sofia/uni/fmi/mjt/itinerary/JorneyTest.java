package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JorneyTest {
    @Test
    void testTotalPriceIsCorrectForBus() {
        City startCity = new City("Sofia", new Location(0, 0));
        City destination = new City("Plovdiv", new Location( 100, 0));

        Journey bus1 = new Journey(VehicleType.BUS, startCity, destination, BigDecimal.valueOf(100));
        Journey bus2 = new Journey(VehicleType.BUS, startCity, destination, BigDecimal.valueOf(80));
        Journey bus3 = new Journey(VehicleType.BUS, startCity, destination, BigDecimal.valueOf(20));

        BigDecimal distance = BigDecimal.valueOf(City.getDistanceInMetres(startCity, destination));

        BigDecimal bus1ExpectedResult = BigDecimal.valueOf(110.0).add(distance);
        BigDecimal bus2ExpectedResult = BigDecimal.valueOf(88.0).add(distance);
        BigDecimal bus3ExpectedResult = BigDecimal.valueOf(22.0).add(distance);

        assertTrue(bus1ExpectedResult.equals(bus1.totalPrice()),
                "Bus expected 210.0, but it was : " + bus1.totalPrice());
        assertTrue(bus2ExpectedResult.equals(bus2.totalPrice()),
                "Bus expected 188.0, but it was : " + bus2.totalPrice());
        assertTrue(bus3ExpectedResult.equals(bus3.totalPrice()),
                "Bus expected 122.0, but it was : " + bus3.totalPrice());
    }

    @Test
    void testTotalPriceIsCorrectForTrain() {
        City startCity = new City("Sofia", new Location(0, 0));
        City destination = new City("Plovdiv", new Location( 100, 0));

        BigDecimal distance = BigDecimal.valueOf(City.getDistanceInMetres(startCity, destination));

        Journey train1 = new Journey(VehicleType.TRAIN, startCity, destination, BigDecimal.valueOf(100));
        Journey train2 = new Journey(VehicleType.TRAIN, startCity, destination, BigDecimal.valueOf(75));
        Journey train3 = new Journey(VehicleType.TRAIN, startCity, destination, BigDecimal.valueOf(4));

        BigDecimal train1ExpectedResult = BigDecimal.valueOf(100).add(distance);
        BigDecimal train2ExpectedResult = BigDecimal.valueOf(75).add(distance);
        BigDecimal train3ExpectedResult = BigDecimal.valueOf(4).add(distance);

        assertTrue(train1ExpectedResult.equals(train1.totalPrice()),
                "Train expected 200, but it was : " + train1.totalPrice());
        assertTrue(train2ExpectedResult.equals(train2.totalPrice()),
                "Train expected 175, but it was : " + train2.totalPrice());
        assertTrue(train3ExpectedResult.equals(train3.totalPrice()),
                "Train expected 104, but it was : " + train3.totalPrice());
    }

    @Test
    void testTotalPriceIsCorrectForPlane() {
        City startCity = new City("Sofia", new Location(0, 0));
        City destination = new City("Plovdiv", new Location( 100, 0));

        BigDecimal distance = BigDecimal.valueOf(City.getDistanceInMetres(startCity, destination));

        Journey plane1 = new Journey(VehicleType.PLANE, startCity, destination, BigDecimal.valueOf(100));
        Journey plane2 = new Journey(VehicleType.PLANE, startCity, destination, BigDecimal.valueOf(175));
        Journey plane3 = new Journey(VehicleType.PLANE, startCity, destination, BigDecimal.valueOf(564));

        BigDecimal plane1ExpectedResult = BigDecimal.valueOf(100).multiply(VehicleType.PLANE.getGreenTax()).add(plane1.price()).add(distance);
        BigDecimal plane2ExpectedResult = BigDecimal.valueOf(175).multiply(VehicleType.PLANE.getGreenTax()).add(plane2.price()).add(distance);
        BigDecimal plane3ExpectedResult = BigDecimal.valueOf(564).multiply(VehicleType.PLANE.getGreenTax()).add(plane3.price()).add(distance);

        assertTrue(plane1ExpectedResult.equals(plane1.totalPrice()),
                "Plane expected 225.00, but it was : " + plane1.totalPrice());
        assertTrue(plane2ExpectedResult.equals(plane2.totalPrice()),
                "Plane expected 318.75, but it was : " + plane2.totalPrice());
        assertTrue(plane3ExpectedResult.equals(plane3.totalPrice()),
                "Plane expected 805.00, but it was : " + plane3.totalPrice());

    }

}
