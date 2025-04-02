package bg.sofia.uni.fmi.mjt.simcity.buildings;

import bg.sofia.uni.fmi.mjt.simcity.property.buildable.BuildableType;

public class CommercialBuilding extends Building {
    public CommercialBuilding(int area, double waterConsumption,
                              double electricityConsumption, double naturalGasConsumption) {
        super(area, waterConsumption, electricityConsumption, naturalGasConsumption);
    }

    @Override
    public BuildableType getType() {
        return BuildableType.COMMERCIAL;
    }
}
