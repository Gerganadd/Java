package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.exception.messages.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {
    private int totalArea;
    private int remainingBuildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        setBuildableArea(buildableArea);
        this.remainingBuildableArea = totalArea;
        this.buildings = new HashMap<>();
    }

    private void setBuildableArea(int buildableArea) {
        if (buildableArea < 0) {
            throw new IllegalArgumentException(ExceptionMessages.NEGATIVE_BUILDABLE_AREA);
        }

        this.totalArea = buildableArea;
    }

    @Override
    public void construct(String address, E buildable) {
        validateAddress(address);
        validateBuilding(buildable);

        validateContainsKey(address);
        validateArea(buildable.getArea());

        this.remainingBuildableArea -= buildable.getArea();

        buildings.put(address, buildable);
    }

    @Override
    public void demolish(String address) {
        validateAddress(address);
        validateDoesNotContainKey(address);

        this.remainingBuildableArea += buildings.get(address).getArea();

        buildings.remove(address);
    }

    @Override
    public void demolishAll() {
        this.remainingBuildableArea = totalArea;
        buildings.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return remainingBuildableArea;
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        validateAll(buildables);

        remainingBuildableArea -= getTotalBuildableAreaOf(buildables);

        buildings.putAll(buildables);
    }

    private void validateAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.COLLECTION_OF_BUILDABLE_NULL_OR_EMPTY);
        }

        for (Map.Entry<String, E> element : buildables.entrySet()) {
            validateAddress(element.getKey());
            validateBuilding(element.getValue());
            validateContainsKey(element.getKey());
        }

        int areas = getTotalBuildableAreaOf(buildables);

        validateArea(areas);
    }

    private int getTotalBuildableAreaOf(Map<String, E> buildables) {
        int sumAreas = 0;

        for (E element : buildables.values()) {
            sumAreas += element.getArea();
        }

        return sumAreas;
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessages.ADDRESS_NULL_OR_EMPTY);
        }
    }

    private void validateBuilding(E buildable) { // to-do change name
        if (buildable == null) {
            throw new IllegalArgumentException(ExceptionMessages.BUILDABLE_NULL);
        }
    }

    private void validateContainsKey(String address) {
        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException(ExceptionMessages.ADDRESS_EXIST);
        }
    }

    private void validateDoesNotContainKey(String address) {
        if (!buildings.containsKey(address)) {
            throw new BuildableNotFoundException(ExceptionMessages.ADDRESS_DOES_NOT_EXIST);
        }
    }

    private void validateArea(int area) {
        if (area > remainingBuildableArea) {
            throw new InsufficientPlotAreaException(ExceptionMessages.INSUFFICIENT_AREA);
        }
    }
}
