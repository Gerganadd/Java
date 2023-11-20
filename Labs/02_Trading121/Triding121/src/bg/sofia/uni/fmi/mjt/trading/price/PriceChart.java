package bg.sofia.uni.fmi.mjt.trading.price;

import bg.sofia.uni.fmi.mjt.trading.CustomMathFunctions;

public class PriceChart implements PriceChartAPI
{
    private double microsoftStockPrice;
    private double googleStockPrice;
    private double amazonStockPrice;
    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice)
    {
        setMicrosoftStockPrice(microsoftStockPrice);
        setGoogleStockPrice(googleStockPrice);
        setAmazonStockPrice(amazonStockPrice);
    }

    @Override
    public double getCurrentPrice(String stockTicker)
    {
        if (stockTicker == null)
            return 0.0;

        double result =
                switch (stockTicker)
                {
                    case "MSFT" -> microsoftStockPrice;
                    case "AMZ" -> amazonStockPrice;
                    case "GOOG" -> googleStockPrice;
                    default -> 0.00;
                };

        return CustomMathFunctions.round(result, 2);
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange)
    {
        if (stockTicker == null || percentChange <= 0)
            return false;

        switch (stockTicker)
        {
            case "MSFT" -> microsoftStockPrice += microsoftStockPrice * percentChange / 100;
            case "AMZ" -> amazonStockPrice += amazonStockPrice * percentChange / 100;
            case "GOOG" -> googleStockPrice += googleStockPrice * percentChange / 100;

            default -> { return false; }
        }

        return true;
    }

    private void setMicrosoftStockPrice(double price)
    {
        if (price < 0)
            price = 0;

        microsoftStockPrice = price;
    }

    private void setGoogleStockPrice(double price)
    {
        if (price < 0)
            price = 0;

        googleStockPrice = price;
    }
    private void setAmazonStockPrice(double price)
    {
        if (price < 0)
            price = 0;

        amazonStockPrice = price;
    }
}
