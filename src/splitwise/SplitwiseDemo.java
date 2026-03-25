package splitwise;

import java.util.Arrays;
import java.util.List;

public class SplitwiseDemo {
    public static void main(String[] args) {
        System.out.println("=== Splitwise Demo ===\n");

        // Create users
        SplitwiseUser alice   = new SplitwiseUser("u1", "Alice", "alice@mail.com");
        SplitwiseUser bob     = new SplitwiseUser("u2", "Bob", "bob@mail.com");
        SplitwiseUser charlie = new SplitwiseUser("u3", "Charlie", "charlie@mail.com");
        SplitwiseUser dave    = new SplitwiseUser("u4", "Dave", "dave@mail.com");

        BalanceSheet sheet = new BalanceSheet();

        // Expense 1: Alice pays $100 for dinner, split equally among 4
        Expense e1 = new Expense("Dinner", 100.0, alice,
                Arrays.asList(
                        new Split(alice), new Split(bob),
                        new Split(charlie), new Split(dave)),
                SplitType.EQUAL);
        sheet.addExpense(e1);
        System.out.println("Added: " + e1);

        // Expense 2: Bob pays $60 for cab, exact split
        Expense e2 = new Expense("Cab ride", 60.0, bob,
                Arrays.asList(
                        new Split(alice, 20),
                        new Split(bob, 10),
                        new Split(charlie, 30)),
                SplitType.EXACT);
        sheet.addExpense(e2);
        System.out.println("Added: " + e2);

        // Expense 3: Charlie pays $200 for hotel, percentage split
        Expense e3 = new Expense("Hotel", 200.0, charlie,
                Arrays.asList(
                        new Split(alice, 25),     // 25%
                        new Split(bob, 25),       // 25%
                        new Split(charlie, 25),   // 25%
                        new Split(dave, 25)),     // 25%
                SplitType.PERCENTAGE);
        sheet.addExpense(e3);
        System.out.println("Added: " + e3);

        // Show balances
        sheet.showBalances();
        sheet.showSimplifiedDebts();
    }
}

