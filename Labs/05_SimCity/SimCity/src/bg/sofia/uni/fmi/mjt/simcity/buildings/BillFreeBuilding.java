package bg.sofia.uni.fmi.mjt.simcity.buildings;

import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

public abstract class BillFreeBuilding implements Buildable { // to-do change name
    private int area;
    public BillFreeBuilding(int area) {
        this.area = area; // to-do make seter
    }

    @Override
    public int getArea() {
        return area;
    }
}
