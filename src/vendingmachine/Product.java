package vendingmachine;

/**
 * VENDING MACHINE LLD
 * ====================
 * Key Concepts:
 *  - State Pattern:  Machine behavior changes based on its current state
 *                    (Idle -> HasMoney -> Dispensing -> Idle)
 *  - SRP:            Each state handles its own logic
 *  - OCP:            Add new states without modifying existing ones
 *
 * States:
 *  IdleState       -> Waiting for money
 *  HasMoneyState   -> Money inserted, waiting for product selection
 *  DispensingState -> Product selected, dispensing item + change
 *
 * Flow:
 *  1. User inserts money      (Idle -> HasMoney)
 *  2. User selects product    (HasMoney -> Dispensing)
 *  3. Machine dispenses item  (Dispensing -> Idle)
 */
public class Product {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + String.format("%.2f", price) + ")";
    }
}

