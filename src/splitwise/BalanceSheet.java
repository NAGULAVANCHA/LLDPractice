package splitwise;

import java.util.*;

/**
 * Tracks net balances between all pairs of users.
 * balance[A][B] > 0 means A owes B that amount.
 */
public class BalanceSheet {
    // balances.get(userId1).get(userId2) = amount userId1 owes userId2
    private final Map<String, Map<String, Double>> balances;
    private final Map<String, SplitwiseUser> userMap;

    public BalanceSheet() {
        balances = new HashMap<>();
        userMap = new HashMap<>();
    }

    public void addExpense(Expense expense) {
        SplitwiseUser payer = expense.getPaidBy();
        userMap.put(payer.getUserId(), payer);

        for (Split split : expense.getSplits()) {
            SplitwiseUser debtor = split.getUser();
            userMap.put(debtor.getUserId(), debtor);

            if (debtor.equals(payer)) continue; // skip payer's own share

            // debtor owes payer
            addBalance(debtor.getUserId(), payer.getUserId(), split.getAmount());
        }
    }

    private void addBalance(String debtorId, String creditorId, double amount) {
        balances.computeIfAbsent(debtorId, k -> new HashMap<>());
        balances.computeIfAbsent(creditorId, k -> new HashMap<>());

        // Add to debtor->creditor
        balances.get(debtorId).merge(creditorId, amount, Double::sum);
        // Subtract from creditor->debtor (net settlement)
        balances.get(creditorId).merge(debtorId, -amount, Double::sum);
    }

    /**
     * Show all non-zero balances (simplified).
     */
    public void showBalances() {
        System.out.println("\n=== Balances ===");
        boolean found = false;
        for (Map.Entry<String, Map<String, Double>> entry : balances.entrySet()) {
            String debtorId = entry.getKey();
            for (Map.Entry<String, Double> inner : entry.getValue().entrySet()) {
                String creditorId = inner.getKey();
                double amount = inner.getValue();
                if (amount > 0.01) { // debtor owes creditor
                    found = true;
                    System.out.printf("  %s owes %s: $%.2f%n",
                            userMap.get(debtorId).getName(),
                            userMap.get(creditorId).getName(),
                            amount);
                }
            }
        }
        if (!found) System.out.println("  All settled up!");
        System.out.println("================");
    }

    /**
     * Minimize transactions using the greedy approach:
     * Compute net amount for each person, then match max creditor with max debtor.
     */
    public void showSimplifiedDebts() {
        System.out.println("\n=== Simplified Debts (Minimized Transactions) ===");

        // Calculate net balance for each user
        Map<String, Double> netBalance = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : balances.entrySet()) {
            String userId = entry.getKey();
            double net = entry.getValue().values().stream().mapToDouble(d -> d).sum();
            netBalance.put(userId, net);
        }

        // Separate into debtors (owe money, net > 0) and creditors (are owed, net < 0)
        List<Map.Entry<String, Double>> debtors = new ArrayList<>();
        List<Map.Entry<String, Double>> creditors = new ArrayList<>();
        for (Map.Entry<String, Double> e : netBalance.entrySet()) {
            if (e.getValue() > 0.01) debtors.add(e);
            else if (e.getValue() < -0.01) creditors.add(e);
        }

        // Greedy matching
        int i = 0, j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<String, Double> debtor = debtors.get(i);
            Map.Entry<String, Double> creditor = creditors.get(j);

            double settle = Math.min(debtor.getValue(), -creditor.getValue());
            System.out.printf("  %s pays %s: $%.2f%n",
                    userMap.get(debtor.getKey()).getName(),
                    userMap.get(creditor.getKey()).getName(),
                    settle);

            debtor.setValue(debtor.getValue() - settle);
            creditor.setValue(creditor.getValue() + settle);

            if (debtor.getValue() < 0.01) i++;
            if (creditor.getValue() > -0.01) j++;
        }
        System.out.println("=================================================");
    }
}

