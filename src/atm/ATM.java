package atm;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private enum State { IDLE, CARD_INSERTED, AUTHENTICATED }

    private State state;
    private Account currentAccount;
    private final Map<String, Account> accounts;
    private final CashDispenser dispenser;

    public ATM(CashDispenser dispenser) {
        this.state = State.IDLE;
        this.accounts = new HashMap<>();
        this.dispenser = dispenser;
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public void insertCard(String accountNumber) {
        if (state != State.IDLE) { System.out.println("  Card already inserted!"); return; }
        Account acc = accounts.get(accountNumber);
        if (acc == null) { System.out.println("  ❌ Account not found!"); return; }
        currentAccount = acc;
        state = State.CARD_INSERTED;
        System.out.println("  Card inserted: " + accountNumber);
    }

    public void enterPin(String pin) {
        if (state != State.CARD_INSERTED) { System.out.println("  Please insert card first!"); return; }
        if (currentAccount.validatePin(pin)) {
            state = State.AUTHENTICATED;
            System.out.println("  ✅ PIN verified");
        } else {
            System.out.println("  ❌ Wrong PIN!");
            ejectCard();
        }
    }

    public void checkBalance() {
        if (state != State.AUTHENTICATED) { System.out.println("  Not authenticated!"); return; }
        System.out.println("  Balance: $" + String.format("%.2f", currentAccount.getBalance()));
    }

    public void withdraw(int amount) {
        if (state != State.AUTHENTICATED) { System.out.println("  Not authenticated!"); return; }
        if (!currentAccount.withdraw(amount)) {
            System.out.println("  ❌ Insufficient balance!");
            return;
        }
        System.out.println("  Dispensing $" + amount + "...");
        if (!dispenser.dispense(amount)) {
            currentAccount.deposit(amount); // rollback
            System.out.println("  ❌ ATM cannot dispense this amount. Reversed.");
        } else {
            System.out.println("  ✅ Withdrawal complete. Balance: $" +
                    String.format("%.2f", currentAccount.getBalance()));
        }
    }

    public void deposit(double amount) {
        if (state != State.AUTHENTICATED) { System.out.println("  Not authenticated!"); return; }
        currentAccount.deposit(amount);
        System.out.println("  ✅ Deposited $" + String.format("%.2f", amount) +
                ". Balance: $" + String.format("%.2f", currentAccount.getBalance()));
    }

    public void ejectCard() {
        currentAccount = null;
        state = State.IDLE;
        System.out.println("  Card ejected.\n");
    }

    public void displayCashStock() {
        System.out.println("ATM Cash Stock:");
        dispenser.displayStock();
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== ATM Machine Demo ===\n");

        // Cash dispenser chain: $100 -> $50 -> $20 -> $10
        CashDispenser d100 = new CashDispenser(100, 10);
        CashDispenser d50 = new CashDispenser(50, 10);
        CashDispenser d20 = new CashDispenser(20, 10);
        CashDispenser d10 = new CashDispenser(10, 10);
        d100.setNext(d50).setNext(d20).setNext(d10);

        ATM atm = new ATM(d100);
        atm.addAccount(new Account("ACC-001", "1234", 5000));
        atm.addAccount(new Account("ACC-002", "5678", 200));

        atm.displayCashStock();

        // Scenario 1: Normal withdrawal
        System.out.println("\n--- Alice withdraws $280 ---");
        atm.insertCard("ACC-001");
        atm.enterPin("1234");
        atm.checkBalance();
        atm.withdraw(280); // 2x$100 + 1x$50 + 1x$20 + 1x$10
        atm.ejectCard();

        // Scenario 2: Wrong PIN
        System.out.println("--- Wrong PIN ---");
        atm.insertCard("ACC-002");
        atm.enterPin("0000");

        // Scenario 3: Insufficient balance
        System.out.println("--- Insufficient balance ---");
        atm.insertCard("ACC-002");
        atm.enterPin("5678");
        atm.withdraw(500);
        atm.ejectCard();

        // Scenario 4: Deposit
        System.out.println("--- Deposit $300 ---");
        atm.insertCard("ACC-002");
        atm.enterPin("5678");
        atm.deposit(300);
        atm.checkBalance();
        atm.ejectCard();

        atm.displayCashStock();
    }
}

