package vendingmachine;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private VendingMachineState state;
    private double balance;
    private ItemSlot selectedSlot;
    private final Map<String, ItemSlot> inventory; // code -> slot

    public VendingMachine() {
        this.state = new IdleState();
        this.balance = 0;
        this.inventory = new HashMap<>();
    }

    // Delegate actions to current state
    public void insertMoney(double amount)    { state.insertMoney(this, amount); }
    public void selectProduct(String code)    { state.selectProduct(this, code); }
    public void dispense()                    { state.dispense(this); }
    public void cancelTransaction()           { state.cancelTransaction(this); }

    // Inventory management
    public void addProduct(String code, Product product, int quantity) {
        inventory.put(code, new ItemSlot(product, quantity));
    }

    public ItemSlot getSlot(String code) { return inventory.get(code); }

    // Getters/Setters for state management
    public VendingMachineState getState() { return state; }
    public void setState(VendingMachineState state) {
        System.out.println("  [State: " + this.state + " -> " + state + "]");
        this.state = state;
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public ItemSlot getSelectedSlot() { return selectedSlot; }
    public void setSelectedSlot(ItemSlot slot) { this.selectedSlot = slot; }

    public void displayProducts() {
        System.out.println("=== Vending Machine Products ===");
        for (Map.Entry<String, ItemSlot> entry : inventory.entrySet()) {
            ItemSlot slot = entry.getValue();
            System.out.println("  [" + entry.getKey() + "] " + slot.getProduct() +
                    " (qty: " + slot.getQuantity() + ")");
        }
        System.out.println("================================");
    }
}

