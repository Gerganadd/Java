package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public class AmazonStockPurchase extends AbstractStockPurchase
{
    private static final String TICKER = "AMZ";

    public AmazonStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit)
    {
        super(quantity, purchaseTimestamp, purchasePricePerUnit);

    }

    @Override
    public String getStockTicker()
    {
        return TICKER;
    }
}
