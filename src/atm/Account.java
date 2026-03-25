package atm;

/**
 * ATM MACHINE LLD
 * =================
 * Key Concepts:
 *  - State Pattern:            ATM states (Idle, CardInserted, Authenticated, Dispensing)
 *  - Chain of Responsibility:  Cash dispenser (dispense $100 bills first, then $50, $20, $10)
 *  - SRP:                      ATM, Account, CashDispenser, CardReader are separate
 *
 * Interview Points:
 *  - State transitions (like Vending Machine, but with authentication)
 *  - Cash dispenser: greedy algorithm using largest denominations first
 *  - Account balance validation
 *  - Transaction logging
 */
public class Account {
    private final String accountNumber;
    private final String pin;
    private double balance;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(String pin) { return this.pin.equals(pin); }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public void deposit(double amount) { balance += amount; }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}

