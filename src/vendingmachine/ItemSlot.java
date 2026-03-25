package vendingmachine;

public class ItemSlot {
    private final Product product;
    private int quantity;

    public ItemSlot(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public boolean isAvailable() { return quantity > 0; }

    public void dispense() {
        if (quantity <= 0) throw new IllegalStateException("Out of stock!");
        quantity--;
    }

    public void restock(int amount) { quantity += amount; }
}

