package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price)
        implements Comparable<Journey> {
    public BigDecimal totalPrice() {
        BigDecimal tax = price.multiply(vehicleType.getGreenTax());
        return price.add(tax);
    }

    @Override
    public int compareTo(Journey o) {
        int compareTotalPrice = totalPrice().compareTo(o.totalPrice());
        int compareCityFromName = from.name().compareTo(o.from.name());
        int compareCityToName = to.name().compareTo(o.to.name());

        if (compareTotalPrice == 0) {
            if (compareCityFromName == 0) {
                return compareCityToName;
            }
            return compareCityFromName;
        }
        return compareTotalPrice;
    }
}
