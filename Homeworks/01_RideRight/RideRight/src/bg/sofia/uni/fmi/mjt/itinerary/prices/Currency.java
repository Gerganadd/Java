package bg.sofia.uni.fmi.mjt.itinerary.prices;

import java.math.BigDecimal;

public enum Currency {
    OFFICIAL(new BigDecimal("20"));

    private BigDecimal value;
    Currency(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
