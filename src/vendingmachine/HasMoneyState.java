package vendingmachine;

public class HasMoneyState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        System.out.println("Added $" + String.format("%.2f", amount) +
                ". Total balance: $" + String.format("%.2f", machine.getBalance()));
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        ItemSlot slot = machine.getSlot(code);
        if (slot == null) {
            System.out.println("Invalid product code: " + code);
            return;
        }
        if (!slot.isAvailable()) {
            System.out.println(slot.getProduct().getName() + " is out of stock!");
            return;
        }
        if (machine.getBalance() < slot.getProduct().getPrice()) {
            System.out.println("Insufficient balance! Need $" +
                    String.format("%.2f", slot.getProduct().getPrice()) +
                    ", have $" + String.format("%.2f", machine.getBalance()));
            return;
        }
        machine.setSelectedSlot(slot);
        machine.setState(new DispensingState());
        machine.dispense(); // auto-dispense
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please select a product first!");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Returning $" + String.format("%.2f", machine.getBalance()));
        machine.setBalance(0);
        machine.setState(new IdleState());
    }

    @Override
    public String toString() { return "HAS_MONEY"; }
}

