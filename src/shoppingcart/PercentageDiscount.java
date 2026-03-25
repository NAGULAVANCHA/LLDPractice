package shoppingcart;

public class PercentageDiscount implements PricingStrategy {
    private final double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double total) {
        return total * (1 - percentage / 100.0);
    }

    @Override
    public String getDescription() {
        return percentage + "% off";
    }
}

