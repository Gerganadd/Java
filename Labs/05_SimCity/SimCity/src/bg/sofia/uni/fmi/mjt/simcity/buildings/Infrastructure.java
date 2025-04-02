package bg.sofia.uni.fmi.mjt.simcity.buildings;

import bg.sofia.uni.fmi.mjt.simcity.property.buildable.BuildableType;

public class Infrastructure extends BillFreeBuilding {
    public Infrastructure(int area) {
        super(area);
    }

    @Override
    public BuildableType getType() {
        return BuildableType.INFRASTRUCTURE;
    }
}
