package paymentgateway;

import java.util.*;

public class PaymentGateway {
    private final Map<String, PaymentMethod> methods = new LinkedHashMap<>();
    private final Map<Integer, Transaction> transactions = new LinkedHashMap<>();
    private FraudChecker fraudChain;

    public void registerMethod(PaymentMethod method) {
        methods.put(method.getName(), method);
    }

    public void setFraudChain(FraudChecker chain) { this.fraudChain = chain; }

    public synchronized Transaction processPayment(String userId, double amount, String methodName) {
        System.out.println("\n💳 Payment: " + userId + " | ₹" + String.format("%.2f", amount) + " via " + methodName);

        PaymentMethod method = methods.get(methodName);
        if (method == null) {
            System.out.println("  ❌ Unknown payment method: " + methodName);
            return null;
        }

        Transaction txn = new Transaction(userId, amount, method);
        transactions.put(txn.getTransactionId(), txn);

        // Fraud checks
        if (fraudChain != null && !fraudChain.check(txn)) {
            txn.setStatus(PaymentStatus.FAILED);
            System.out.println("  ❌ Transaction REJECTED by fraud checks");
            return txn;
        }

        // Process payment
        txn.setStatus(PaymentStatus.PROCESSING);
        boolean success = method.processPayment(amount, "TXN-" + txn.getTransactionId());
        txn.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        System.out.println("  Result: " + txn);
        return txn;
    }

    public Transaction refund(int transactionId) {
        Transaction txn = transactions.get(transactionId);
        if (txn == null) { System.out.println("  ❌ Txn not found: " + transactionId); return null; }
        if (txn.getStatus() != PaymentStatus.SUCCESS) {
            System.out.println("  ❌ Can only refund successful txns. Current: " + txn.getStatus());
            return txn;
        }

        System.out.println("\n🔄 Refund for Txn#" + transactionId);
        boolean refunded = txn.getMethod().refund("TXN-" + transactionId, txn.getAmount());
        if (refunded) txn.setStatus(PaymentStatus.REFUNDED);
        System.out.println("  Result: " + txn);
        return txn;
    }

    public void showTransactions() {
        System.out.println("\n=== Transaction History ===");
        transactions.values().forEach(t -> System.out.println("  " + t));
    }
}

