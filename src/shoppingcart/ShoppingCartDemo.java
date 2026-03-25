package shoppingcart;

public class ShoppingCartDemo {
    public static void main(String[] args) {
        System.out.println("=== Shopping Cart Demo ===\n");

        Product laptop = new Product("p1", "Laptop", 999.99, 10, "Electronics");
        Product phone  = new Product("p2", "Phone", 499.99, 5, "Electronics");
        Product book   = new Product("p3", "Clean Code Book", 45.00, 20, "Books");

        Cart cart = new Cart("user-alice");

        cart.addItem(laptop, 1);
        cart.addItem(phone, 2);
        cart.addItem(book, 1);
        cart.display();

        // Apply 10% discount
        System.out.println("\n--- Apply 10% discount ---");
        cart.setDiscount(new PercentageDiscount(10));
        cart.display();

        // Change to flat $50 off
        System.out.println("\n--- Switch to $50 flat off ---");
        cart.setDiscount(new FlatDiscount(50));
        cart.display();

        // Update quantity
        System.out.println("\n--- Update phone qty to 1 ---");
        cart.updateQuantity("p2", 1);
        cart.display();

        // Checkout
        System.out.println("\n--- Checkout ---");
        Order order = cart.checkout();
        System.out.println("\n✅ " + order);

        // Cart is now empty
        cart.display();

        // Stock reduced
        System.out.println("\nStock after order:");
        System.out.println("  " + laptop);
        System.out.println("  " + phone);
        System.out.println("  " + book);
    }
}

