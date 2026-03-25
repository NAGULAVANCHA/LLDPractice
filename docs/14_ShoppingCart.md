# Problem 14: Shopping Cart

| Pattern | Why |
|---|---|
| **Strategy** | PricingStrategy — swappable discount algorithms |
| **SRP** | Product, CartItem, Cart, Order each have one job |

## Strategy Pattern in Action
```java
interface PricingStrategy {
    double applyDiscount(double total);
}

class NoDiscount        → return total;
class PercentageDiscount → return total * (1 - pct/100);
class FlatDiscount       → return total - flatAmount;
```

### Swap at Runtime
```java
cart.setDiscount(new PercentageDiscount(20)); // 20% off
cart.setDiscount(new FlatDiscount(50));        // $50 off
```

## Cart Design
- `Map<productId, CartItem>` — O(1) add/remove/update
- **Stock check** on add (prevent overselling)
- **Atomic checkout**: reduce stock for all items, create Order, clear cart

## Entities
```
Product  → id, name, price, stockQuantity
CartItem → product + quantity (subtotal = price × qty)
Cart     → userId + items + pricingStrategy
Order    → snapshot of cart at checkout time
```

📁 `src/shoppingcart/`

