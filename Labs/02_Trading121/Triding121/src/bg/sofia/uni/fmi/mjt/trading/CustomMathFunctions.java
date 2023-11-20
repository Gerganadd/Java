package bg.sofia.uni.fmi.mjt.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomMathFunctions
{
    public static double round(double value, int places)
    {
        if (places < 0) return 0.00;

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
