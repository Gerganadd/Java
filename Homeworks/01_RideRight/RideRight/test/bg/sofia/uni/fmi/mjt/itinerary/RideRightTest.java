package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RideRightTest {

    @Test
    void testFindCheapestPathWithoutTransfersNoExistDestination() {
        List<Journey> journeys = new ArrayList<>();
        journeys.add(Journies.VARNA_SOFIA_PLANE);
        journeys.add(Journies.VARNA_PLOVDIV_TRAIN);
        journeys.add(Journies.VARNA_SOFIA_TRAIN);
        journeys.add(Journies.DIMITOVGRAD_BURGAS_TRAIN);

        RideRight center = new RideRight(journeys);

        assertThrows(NoPathToDestinationException.class,
                () -> center.findCheapestPath(Cities.VARNA, Cities.DIMITROVGRAD, false),
                "NoPathToDestinationException was expected but not throw");
        assertThrows(NoPathToDestinationException.class,
                () -> center.findCheapestPath(Cities.PLOVDIV, Cities.SOFIA, false),
                "NoPathToDestinationException was expected but not throw");
        assertThrows(NoPathToDestinationException.class,
                () -> center.findCheapestPath(Cities.SOFIA, Cities.VARNA, false),
                "NoPathToDestinationException was expected but not throw");
    }

}
