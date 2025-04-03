package bg.sofia.uni.fmi.mjt.itinerary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CityTest {

    @Test
    void testGetDistanceInMetresIsCorrectBetweenTwoCities()
    {
        City city1 = new City("Sofia", new Location(120, 30));
        City city2 = new City("Plovdiv", new Location(110, 80));

        long expected = 60;

        assertTrue(City.getDistanceInMetres(city1, city2) == expected);
        assertTrue(City.getDistanceInMetres(city2, city1) == expected);
    }

    @Test
    void testGetDistanceInMetresIsCorrectBetweenTwoCitiesNegativeCoordinates()
    {
        City city1 = new City("Sofia", new Location(20, 30));
        City city2 = new City("Plovdiv", new Location(-40, 80));

        long expected = 110;

        assertTrue(City.getDistanceInMetres(city1, city2) == expected);
        assertTrue(City.getDistanceInMetres(city2, city1) == expected);
    }

    @Test
    void testGetDistanceInMetresIsCorrect() {
        City city1 = new City("Sofia", new Location(120, 30));
        City city2 = new City("Plovdiv", new Location(110, 80));
        City city3 = new City("Veliko Turnovo", new Location(70, 90));

        long expected31 = 110;
        long expected23 = 50;

        assertTrue(City.getDistanceInMetres(city3, city1) == expected31);
        assertTrue(City.getDistanceInMetres(city2, city3) == expected23);
    }
}
