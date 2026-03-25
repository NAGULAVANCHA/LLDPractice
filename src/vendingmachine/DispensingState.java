package vendingmachine;

public class DispensingState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Please wait, dispensing in progress...");
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        System.out.println("Please wait, dispensing in progress...");
    }

    @Override
    public void dispense(VendingMachine machine) {
        ItemSlot slot = machine.getSelectedSlot();
        Product product = slot.getProduct();

        slot.dispense();
        double change = machine.getBalance() - product.getPrice();

        System.out.println("Dispensed: " + product.getName());
        if (change > 0) {
            System.out.println("Change returned: $" + String.format("%.2f", change));
        }

        // Reset machine
        machine.setBalance(0);
        machine.setSelectedSlot(null);
        machine.setState(new IdleState());
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Cannot cancel, dispensing in progress...");
    }

    @Override
    public String toString() { return "DISPENSING"; }
}

