package paymentgateway;

import java.util.Random;

public class UPIPayment implements PaymentMethod {
    private final Random random = new Random(42);

    @Override
    public boolean processPayment(double amount, String txnId) {
        System.out.println("  [UPI] Processing ₹" + String.format("%.2f", amount) + " via UPI...");
        boolean success = random.nextDouble() < 0.90;
        System.out.println("  [UPI] " + (success ? "✅ Payment collected" : "❌ UPI timeout"));
        return success;
    }

    @Override
    public boolean refund(String txnId, double amount) {
        System.out.println("  [UPI] Refunding ₹" + String.format("%.2f", amount) + "...");
        System.out.println("  [UPI] ✅ Refund credited to UPI");
        return true;
    }

    @Override public String getName() { return "UPI"; }
}

