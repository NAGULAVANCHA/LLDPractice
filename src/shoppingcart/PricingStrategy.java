package shoppingcart;

/**
 * Strategy Pattern for pricing/discount.
 */
public interface PricingStrategy {
    double applyDiscount(double totalAmount);
    String getDescription();
}

