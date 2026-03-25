package shoppingcart;

public class FlatDiscount implements PricingStrategy {
    private final double flatOff;

    public FlatDiscount(double flatOff) {
        this.flatOff = flatOff;
    }

    @Override
    public double applyDiscount(double total) {
        return Math.max(0, total - flatOff);
    }

    @Override
    public String getDescription() {
        return "$" + String.format("%.2f", flatOff) + " flat off";
    }
}

