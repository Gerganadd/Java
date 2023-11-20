package bg.sofia.uni.fmi.mjt.trading.stock;

import bg.sofia.uni.fmi.mjt.trading.CustomMathFunctions;

import java.time.LocalDateTime;

public abstract class AbstractStockPurchase implements StockPurchase
{
    protected int quantity;
    protected LocalDateTime purchaseTimestamp;
    protected double purchasePricePerUnit;

    protected AbstractStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit)
    {
        //to-do make setters

        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity()
    {
        return quantity;
    }
    @Override
    public LocalDateTime getPurchaseTimestamp()
    {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit()
    {
        return CustomMathFunctions.round(purchasePricePerUnit, 2);
    }

    @Override
    public double getTotalPurchasePrice()
    {
        double totalSum = getPurchasePricePerUnit() * quantity;

        return CustomMathFunctions.round(totalSum, 2);
    }

}

