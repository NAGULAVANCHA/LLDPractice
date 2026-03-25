package shoppingcart;

import java.util.*;

public class Cart {
    private final String userId;
    private final Map<String, CartItem> items; // productId -> CartItem
    private PricingStrategy pricingStrategy;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new LinkedHashMap<>();
        this.pricingStrategy = new NoDiscount();
    }

    public void addItem(Product product, int quantity) {
        if (!product.isInStock(quantity)) {
            System.out.println("  ❌ " + product.getName() + " insufficient stock!");
            return;
        }
        CartItem existing = items.get(product.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(product.getProductId(), new CartItem(product, quantity));
        }
        System.out.println("  Added " + product.getName() + " x" + quantity);
    }

    public void removeItem(String productId) {
        CartItem removed = items.remove(productId);
        if (removed != null) System.out.println("  Removed " + removed.getProduct().getName());
    }

    public void updateQuantity(String productId, int newQty) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (newQty <= 0) { removeItem(productId); return; }
            item.setQuantity(newQty);
        }
    }

    public void setDiscount(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        System.out.println("  Applied: " + strategy.getDescription());
    }

    public double getSubtotal() {
        return items.values().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public double getTotal() {
        return pricingStrategy.applyDiscount(getSubtotal());
    }

    public Order checkout() {
        if (items.isEmpty()) { System.out.println("  Cart is empty!"); return null; }
        // Reduce stock for all items
        for (CartItem item : items.values()) {
            item.getProduct().reduceStock(item.getQuantity());
        }
        Order order = new Order(userId, new ArrayList<>(items.values()), getSubtotal(), getTotal(), pricingStrategy.getDescription());
        items.clear();
        return order;
    }

    public void display() {
        System.out.println("\n🛒 Cart (" + userId + "):");
        if (items.isEmpty()) { System.out.println("  (empty)"); return; }
        items.values().forEach(i -> System.out.println("  " + i));
        System.out.println("  Subtotal: $" + String.format("%.2f", getSubtotal()));
        System.out.println("  Discount: " + pricingStrategy.getDescription());
        System.out.println("  Total:    $" + String.format("%.2f", getTotal()));
    }
}

