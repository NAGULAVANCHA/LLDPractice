package paymentgateway;

import java.util.Random;

public class CardPayment implements PaymentMethod {
    private final Random random = new Random(99);

    @Override
    public boolean processPayment(double amount, String txnId) {
        System.out.println("  [CARD] Authorizing ₹" + String.format("%.2f", amount) + "...");
        boolean success = random.nextDouble() < 0.85;
        System.out.println("  [CARD] " + (success ? "✅ Card charged" : "❌ Card declined"));
        return success;
    }

    @Override
    public boolean refund(String txnId, double amount) {
        System.out.println("  [CARD] Refunding ₹" + String.format("%.2f", amount) + " to card...");
        System.out.println("  [CARD] ✅ Refund initiated (3-5 business days)");
        return true;
    }

    @Override public String getName() { return "CARD"; }
}

