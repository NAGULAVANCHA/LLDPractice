# Problem 14: Shopping Cart — Complete Guide

---

## Part 1: Understanding the Problem

An online shopping cart where users add products, apply discounts, and checkout.

### Requirements
- ✓ Add/remove/update items in cart
- ✓ Swappable discount strategies (percentage off, flat off, no discount)
- ✓ Stock validation on add and checkout
- ✓ Checkout creates an immutable Order snapshot

---

## Part 2: The Key Insight — Strategy Pattern for Pricing

### The Problem
How to support different discounts without if/else chains?
```java
// BAD — hardcoded discounts
double getTotal() {
    if (discountType == "PERCENTAGE") return subtotal * (1 - pct/100);
    else if (discountType == "FLAT") return subtotal - flatAmount;
    else if (discountType == "BOGO") { ... }
    // keeps growing!
}
```

### The Solution — Strategy Pattern
```java
interface PricingStrategy {
    double applyDiscount(double totalAmount);
    String getDescription();
}

class NoDiscount implements PricingStrategy {
    double applyDiscount(double total) { return total; }
}

class PercentageDiscount implements PricingStrategy {
    private final double percent;
    double applyDiscount(double total) { return total * (1 - percent / 100); }
}

class FlatDiscount implements PricingStrategy {
    private final double amount;
    double applyDiscount(double total) { return Math.max(0, total - amount); }
}
```

### Swap at Runtime
```java
cart.setDiscount(new PercentageDiscount(20)); // 20% off
cart.getTotal(); // applies 20% off

cart.setDiscount(new FlatDiscount(50));        // switch to $50 off
cart.getTotal(); // applies $50 off
```

**Adding a new discount (BOGO, tiered, etc.) = new class. ZERO changes to Cart.**

---

## Part 3: The Code — Explained

### Product — Stock Management
```java
public class Product {
    private final String productId, name;
    private double price;
    private int stock;

    public boolean isInStock(int qty) { return stock >= qty; }
    public void reduceStock(int qty)  { stock -= qty; }
}
```

### Cart — The Main Container
```java
public class Cart {
    private final Map<String, CartItem> items;  // productId → CartItem
    private PricingStrategy pricingStrategy;

    public void addItem(Product product, int quantity) {
        if (!product.isInStock(quantity)) { "Insufficient stock!"; return; }
        CartItem existing = items.get(product.getProductId());
        if (existing != null)
            existing.setQuantity(existing.getQuantity() + quantity);
        else
            items.put(product.getProductId(), new CartItem(product, quantity));
    }

    public double getSubtotal() {
        return items.values().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public double getTotal() {
        return pricingStrategy.applyDiscount(getSubtotal());
    }

    public Order checkout() {
        if (items.isEmpty()) return null;
        for (CartItem item : items.values())
            item.getProduct().reduceStock(item.getQuantity());  // reduce stock
        Order order = new Order(userId, items, getSubtotal(), getTotal(), ...);
        items.clear();  // cart is now empty
        return order;
    }
}
```

### Order — Immutable Snapshot
```java
public class Order {
    private final int orderId;
    private final List<CartItem> items;   // snapshot of cart at checkout
    private final double subtotal, total;
    private final String discountApplied;
    private final LocalDateTime orderTime;
}
```

**Cart is MUTABLE** (add/remove/update). **Order is IMMUTABLE** (created once, never changed). This is a key interview distinction.

---

## Part 4: Data Flow — Full Purchase

```
1. cart.addItem(Laptop, 1)   → items: {p1: Laptop×1}
2. cart.addItem(Phone, 2)    → items: {p1: Laptop×1, p2: Phone×2}
3. cart.getSubtotal()        → 999.99 + (499.99 × 2) = $1999.97

4. cart.setDiscount(new PercentageDiscount(10))  → 10% off
5. cart.getTotal()           → $1999.97 × 0.90 = $1799.97

6. cart.checkout()
   → Laptop stock: 10 → 9
   → Phone stock: 5 → 3
   → Create Order#1 ($1799.97)
   → cart items cleared → empty

7. cart.display() → "(empty)"
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Stacking discounts? | Create `StackedDiscount` that applies multiple strategies in sequence. |
| Coupon codes? | Create `CouponDiscount` strategy. Validate coupon code, expiry, usage limits. |
| Price changes after adding to cart? | Cart reads `product.getPrice()` on getSubtotal(). Price is always current. For guaranteed price, store price at add-time in CartItem. |
| Persistent cart? | Serialize cart to database. Load on user login. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Strategy** | PricingStrategy — swappable discount algorithms |
| **SRP** | Product manages stock, Cart manages items, Order is a snapshot |
| **OCP** | Add new discounts without modifying Cart |
| **Immutability** | Order is frozen at checkout time |

---

📁 **Source code:** `src/shoppingcart/`
