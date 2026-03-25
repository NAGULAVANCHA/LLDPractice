package vendingmachine;

/**
 * State interface — each state defines what happens on
 * insertMoney, selectProduct, and dispense.
 */
public interface VendingMachineState {
    void insertMoney(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, String code);
    void dispense(VendingMachine machine);
    void cancelTransaction(VendingMachine machine);
}

