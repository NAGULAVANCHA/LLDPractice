package vendingmachine;

public class IdleState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Money inserted: $" + String.format("%.2f", amount));
        machine.setBalance(machine.getBalance() + amount);
        machine.setState(new HasMoneyState());
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        System.out.println("Please insert money first!");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please insert money and select a product first!");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("No transaction to cancel.");
    }

    @Override
    public String toString() { return "IDLE"; }
}

