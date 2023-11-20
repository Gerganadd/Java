import bg.sofia.uni.fmi.mjt.trading.CustomMathFunctions;
import bg.sofia.uni.fmi.mjt.trading.Portfolio;
import bg.sofia.uni.fmi.mjt.trading.price.PriceChart;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.trading.CustomMathFunctions.round;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args)
    {

        Portfolio pf = new Portfolio("Pesho", new PriceChart(1.1, 1.2, 1.3), 200, 5);
        StockPurchase st1 = pf.buyStock("AMZ", 5);
        StockPurchase st2 = pf.buyStock("GOOG", 12);
        StockPurchase st3 = pf.buyStock("MSFT", 10);
        StockPurchase st4 = pf.buyStock("AMZ", 8);
        StockPurchase st5 = pf.buyStock("GOOG", 10);
        StockPurchase st6 = pf.buyStock("AMZ", 1);

        StockPurchase[] arr = {st1, st2, st3, st4, st5, st6};

        Portfolio pf2 = new Portfolio("Gogo", new PriceChart(1,7,2.3), arr, 10000, 10);
        Portfolio pf3 = new Portfolio("Gogo", new PriceChart(1,7,2.3), null, 10000, 10);

        pf2.buyStock("AMZ", 10);

        Arrays.stream(pf2.getAllPurchases()).forEach(x -> System.out.println(x.getStockTicker() + " " + x.getTotalPurchasePrice()));

        System.out.println("______________________________________________________________________________________________");

        Arrays.stream(pf.getAllPurchases()).forEach(x -> System.out.println(x.getStockTicker() + " " + x.getTotalPurchasePrice()));
    }


}