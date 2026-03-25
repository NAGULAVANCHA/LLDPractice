package shoppingcart;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private static int counter = 0;
    private final int orderId;
    private final String userId;
    private final List<CartItem> items;
    private final double subtotal;
    private final double total;
    private final String discountApplied;
    private final LocalDateTime orderTime;

    public Order(String userId, List<CartItem> items, double subtotal, double total, String discount) {
        this.orderId = ++counter;
        this.userId = userId;
        this.items = items;
        this.subtotal = subtotal;
        this.total = total;
        this.discountApplied = discount;
        this.orderTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order#").append(orderId).append(" | User: ").append(userId).append(" | ").append(orderTime).append("\n");
        for (CartItem item : items) sb.append("    ").append(item).append("\n");
        sb.append("    Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("    Discount: ").append(discountApplied).append("\n");
        sb.append("    TOTAL:    $").append(String.format("%.2f", total));
        return sb.toString();
    }
}

