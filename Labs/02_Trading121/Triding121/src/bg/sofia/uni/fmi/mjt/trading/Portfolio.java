package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Portfolio implements PortfolioAPI
{
    private int currentSize;
    private String owner;
    private PriceChartAPI priceChart;
    private StockPurchase[] stockPurchases;
    private double budget;
    private int maxSize;
    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize)
    {
        this(owner, priceChart, new StockPurchase[maxSize], budget, maxSize);
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget, int maxSize)
    {
        setOwner(owner);
        setPriceChart(priceChart);
        setBudget(budget);
        setMaxSize(maxSize);
        setStockPurchases(stockPurchases);
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity)
    {
        if (stockTicker == null || quantity <= 0 ||  currentSize == maxSize)
        {
            return null;
        }

        double price = priceChart.getCurrentPrice(stockTicker);
        double totalPrice = price * quantity;

        if (totalPrice > budget)
        {
            return null;
        }

        StockPurchase newPurchase =
                switch (stockTicker)
                {
                    case "MSFT" -> new MicrosoftStockPurchase(quantity, LocalDateTime.now(), price);
                    case "AMZ" -> new AmazonStockPurchase(quantity, LocalDateTime.now(), price);
                    case "GOOG" -> new GoogleStockPurchase(quantity, LocalDateTime.now(), price);
                    default -> null;
                };

        if (newPurchase != null)
        {
            stockPurchases[currentSize] = newPurchase;
            currentSize++;

            budget -= totalPrice;

            priceChart.changeStockPrice(stockTicker, 5);
        }

        return newPurchase;
    }

    @Override
    public StockPurchase[] getAllPurchases()
    {
       return Arrays.copyOfRange(stockPurchases, 0, currentSize);
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp)
    {
        if (startTimestamp == null || endTimestamp == null)
            return null;

        if (startTimestamp.isAfter(endTimestamp))
            return null;

        StockPurchase[] result = new StockPurchase[currentSize];

        int counter = 0;

        for (StockPurchase currentStock : stockPurchases)
        {
            if (currentStock == null)
                break;

            if (currentStock.getPurchaseTimestamp().isAfter(startTimestamp)
                    && currentStock.getPurchaseTimestamp().isBefore(endTimestamp))
            {
                result[counter] = currentStock;
                counter++;
            }
        }

        return Arrays.copyOfRange(result, 0, counter);
    }

    @Override
    public double getNetWorth()
    {
        if (stockPurchases.length == 0)
            return 0.0;

        double sum = 0;

        for (int i = 0; i < currentSize; i++)
        {
            if (stockPurchases[i] == null)
                continue;

            sum += stockPurchases[i].getQuantity() * priceChart.getCurrentPrice(stockPurchases[i].getStockTicker());
        }

        return CustomMathFunctions.round(sum, 2);
    }

    @Override
    public double getRemainingBudget()
    {
        return CustomMathFunctions.round(budget, 2);
    }

    @Override
    public String getOwner() {
        return owner;
    }

    private void setOwner(String owner)
    {
        if (owner == null)
            owner = "unknown";

        this.owner = owner;
    }

    private void setPriceChart(PriceChartAPI priceChart)
    {
        this.priceChart = priceChart;
    }

    private void setStockPurchases(StockPurchase[] stockPurchases)
    {
        this.stockPurchases = new StockPurchase[maxSize];

        if (stockPurchases == null)
        {
            currentSize = 0;
            return;
        }

        int counter = 0;

        for (int i = 0; i < stockPurchases.length; i++)
        {
            if (stockPurchases[i] == null)
                counter++;

            this.stockPurchases[i] = stockPurchases[i];
        }

        this.currentSize = stockPurchases.length - counter;

    }

    private void setBudget(double budget) {
        this.budget = budget;
    }

    private void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
