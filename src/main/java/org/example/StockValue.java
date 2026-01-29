package org.example;

public class StockValue {

    private double quantity;
    private UnitEnum unit;

    public StockValue(double quantity, UnitEnum unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return quantity + " " + unit;
    }
}
