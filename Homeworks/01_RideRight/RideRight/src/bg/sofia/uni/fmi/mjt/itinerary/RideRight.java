package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.prices.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {
    private Map<City, Set<Journey>> info;

    public RideRight(List<Journey> schedule) {
        info = new HashMap<>();
        parseInfo(schedule);
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
            throws CityNotKnownException, NoPathToDestinationException {
        validateCity(start);
        validateCity(destination);

        if (!allowTransfer) {
            return getDirectPath(start, destination);
        }

        List<Journey> result = calculateShortestPath(start, destination);

        checkHasValidPath(result);

        return result;
    }

    private List<Journey> calculateShortestPath(City start, City destination) throws NoPathToDestinationException {
        List<Journey> result = new ArrayList<>();
        Map<BigDecimal, Journey> availablePaths = new TreeMap<>();
        Map<City, BigDecimal> visitedCities = new HashMap<>();

        visitedCities.put(start, BigDecimal.valueOf(0));

        addNewPathsFromStartCity(availablePaths, visitedCities, start, destination);

        Map.Entry<BigDecimal, Journey> cheapestEntry = getCheapesUnvisitedEntry(availablePaths, visitedCities);
        Journey cheapestJourney = cheapestEntry.getValue();
        BigDecimal cheapestJourneyKey = cheapestEntry.getKey();

        while (!cheapestJourney.to().equals(destination)) {
            if (visitedCities.containsKey(cheapestJourney.to())) {
                updateVisitedCities(visitedCities, cheapestJourney);
                updateResult(result, cheapestJourney);
            }

            BigDecimal newPrice = visitedCities.get(cheapestJourney.from());
            newPrice = newPrice.add(cheapestJourney.totalPrice());
            visitedCities.put(cheapestJourney.to(), newPrice);

            result.add(cheapestJourney);
            availablePaths.remove(cheapestJourneyKey);

            addNewPathsFromStartCity(availablePaths, visitedCities, cheapestJourney.to(), destination);

            cheapestEntry = getCheapesUnvisitedEntry(availablePaths, visitedCities);
            cheapestJourney = cheapestEntry.getValue();
            cheapestJourneyKey = cheapestEntry.getKey();
        }

        result.add(cheapestJourney);

        return result;
    }

    private void updateVisitedCities(Map<City, BigDecimal> visitedCities, Journey journey) {
        BigDecimal price = visitedCities.get(journey.from());
        price = price.add(journey.totalPrice());

        visitedCities.put(journey.to(), price);
    }

    private void updateResult(List<Journey> result, Journey journey) {
        while (!result.isEmpty()) {
            Journey lastJourney = result.getLast();
            result.removeLast();

            if (lastJourney.from().equals(journey.from())) {
                return;
            }
        }
    }

    private BigDecimal calculateKey(Journey journey, City destination, BigDecimal priceFromStart) {
        BigDecimal key = journey.totalPrice().add(priceFromStart);
        BigDecimal distanceToWantedDestination = calculateDistancePrice(journey.to(), destination);

        return key.add(distanceToWantedDestination);
    }

    private BigDecimal calculateDistancePrice(City start, City destination) {
        double distanceToWantedDestination = City.getDistanceInKilometres(start, destination);
        BigDecimal distance = BigDecimal.valueOf(distanceToWantedDestination);

        return distance.multiply(Currency.OFFICIAL.getValue());
    }

    private void addNewPathsFromStartCity(Map<BigDecimal, Journey> collection, Map<City, BigDecimal> visitedCities,
                                          City start, City destination) {
        BigDecimal priceFromStart = visitedCities.get(start);
        for (Journey journey : info.get(start)) {
            BigDecimal key = calculateKey(journey, destination, priceFromStart);

            collection.put(key, journey);
        }
    }

    private Map.Entry<BigDecimal, Journey> getCheapesUnvisitedEntry
            (Map<BigDecimal, Journey> paths, Map<City, BigDecimal> visited) throws NoPathToDestinationException {
        for (Map.Entry<BigDecimal, Journey> journeyEntry : paths.entrySet()) {
            if (!visited.containsKey(journeyEntry.getValue().to())) {
                return journeyEntry;
            }
        }

        throw new NoPathToDestinationException(ExceptionMessages.NO_PATH);
    }

    private SequencedCollection<Journey> getDirectPath(City from, City to) throws NoPathToDestinationException {
        List<Journey> result = new ArrayList<>();

        for (Journey journey : info.get(from)) {
            if (journey.to().equals(to)) {
                result.add(journey);
                break;
            }
        }

        checkHasValidPath(result);

        return result;
    }

    private void checkHasValidPath(List<Journey> paths) throws NoPathToDestinationException {
        if (paths.isEmpty()) {
            throw new NoPathToDestinationException(ExceptionMessages.NO_PATH);
        }

        City startCity = paths.getFirst().from();
        City endCity = paths.getLast().to();

        for (int i = paths.size() - 1; i > 0; i--) {
            if (!paths.get(i).from().equals(paths.get(i - 1).to())) {
                paths.remove(i - 1);
            }
        }

        if (!paths.getFirst().from().equals(startCity) || !paths.getLast().to().equals(endCity)) {
            throw new NoPathToDestinationException(ExceptionMessages.NO_PATH);
        }
    }

    private void validateCity(City city) throws CityNotKnownException {
        if (city == null) {
            throw new IllegalArgumentException(ExceptionMessages.CITY_NULL);
        }
        if (!info.containsKey(city)) {
            throw new CityNotKnownException(city.name() + ExceptionMessages.DOES_NOT_EXIST);
        }
    }

    private void parseInfo(List<Journey> schedule) {
        for (Journey journey : schedule) {
            if (!info.containsKey(journey.from())) {
                info.put(journey.from(), new TreeSet<>());
            }
            if (!info.containsKey(journey.to())) {
                info.put(journey.to(), new TreeSet<>());
            }

            info.get(journey.from()).add(journey);
        }
    }
}
