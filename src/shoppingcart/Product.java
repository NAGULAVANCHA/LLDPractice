package shoppingcart;

/**
 * ONLINE SHOPPING CART LLD
 * =========================
 * Key Concepts:
 *  - Strategy Pattern:  Pricing strategies (flat discount, percentage, buy-one-get-one)
 *  - Observer Pattern:  Notify on price drops, stock changes
 *  - SRP:               Product, Cart, CartItem, Order, PricingStrategy separate
 *  - OCP:               Add new discount types without changing Cart
 *
 * Interview Points:
 *  - Cart is per-user, stateful
 *  - Inventory check on checkout
 *  - Multiple discount strategies can stack or be exclusive
 *  - Order vs Cart (cart is mutable, order is immutable snapshot)
 */
public class Product {
    private final String productId;
    private final String name;
    private double price;
    private int stock;
    private final String category;

    public Product(String productId, String name, double price, int stock, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public boolean isInStock(int qty) { return stock >= qty; }

    public void reduceStock(int qty) {
        if (stock < qty) throw new IllegalStateException("Insufficient stock!");
        stock -= qty;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return name + " ($" + String.format("%.2f", price) + ") [stock:" + stock + "]";
    }
}

