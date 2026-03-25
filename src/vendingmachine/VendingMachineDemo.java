package vendingmachine;

public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();

        // Stock the machine
        vm.addProduct("A1", new Product("Coke", 1.50), 5);
        vm.addProduct("A2", new Product("Pepsi", 1.25), 3);
        vm.addProduct("B1", new Product("Chips", 2.00), 2);
        vm.addProduct("B2", new Product("Candy", 0.75), 10);

        vm.displayProducts();

        // Scenario 1: Normal purchase
        System.out.println("\n--- Scenario 1: Buy Coke ---");
        vm.insertMoney(2.00);
        vm.selectProduct("A1");

        // Scenario 2: Insufficient funds
        System.out.println("\n--- Scenario 2: Insufficient funds ---");
        vm.insertMoney(1.00);
        vm.selectProduct("B1"); // costs $2.00
        vm.insertMoney(1.50);   // add more
        vm.selectProduct("B1"); // now enough

        // Scenario 3: Cancel transaction
        System.out.println("\n--- Scenario 3: Cancel ---");
        vm.insertMoney(5.00);
        vm.cancelTransaction();

        // Scenario 4: Try selecting without money
        System.out.println("\n--- Scenario 4: No money ---");
        vm.selectProduct("A1");
    }
}

