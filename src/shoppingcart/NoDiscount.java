package shoppingcart;

public class NoDiscount implements PricingStrategy {
    @Override public double applyDiscount(double total) { return total; }
    @Override public String getDescription() { return "No discount"; }
}

