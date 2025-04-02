package bg.sofia.uni.fmi.mjt.simcity.buildings;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

public abstract class Building implements Billable {
    private double waterConsumption;
    private double electricityConsumption;
    private double naturalGasConsumption;
    private int area;

    public Building(int area, double waterConsumption, double electricityConsumption, double naturalGasConsumption) {
        this.area = area;
        this.waterConsumption = waterConsumption;
        this.electricityConsumption = electricityConsumption;
        this.naturalGasConsumption = naturalGasConsumption;
    }

    @Override
    public double getWaterConsumption() {
        return waterConsumption;
    }

    @Override
    public double getElectricityConsumption() {
        return electricityConsumption;
    }

    @Override
    public double getNaturalGasConsumption() {
        return naturalGasConsumption;
    }

    @Override
    public int getArea() {
        return area;
    }
}
