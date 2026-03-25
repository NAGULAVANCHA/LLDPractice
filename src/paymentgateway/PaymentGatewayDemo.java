package paymentgateway;

public class PaymentGatewayDemo {
    public static void main(String[] args) {
        System.out.println("=== Payment Gateway / UPI Switch Demo ===\n");

        PaymentGateway gateway = new PaymentGateway();
        gateway.registerMethod(new UPIPayment());
        gateway.registerMethod(new CardPayment());
        gateway.registerMethod(new NetBankingPayment());

        // Fraud chain: AmountLimit(10000) → Velocity(3/min)
        AmountLimitChecker amtCheck = new AmountLimitChecker(10000);
        VelocityChecker velCheck = new VelocityChecker(3);
        amtCheck.setNext(velCheck);
        gateway.setFraudChain(amtCheck);

        // Normal payments
        gateway.processPayment("user-alice", 500, "UPI");
        gateway.processPayment("user-bob", 2500, "CARD");
        gateway.processPayment("user-alice", 1200, "NETBANKING");

        // Exceeds amount limit
        gateway.processPayment("user-charlie", 15000, "UPI");

        // Velocity check (Alice's 4th txn in a minute)
        gateway.processPayment("user-alice", 100, "UPI");
        gateway.processPayment("user-alice", 200, "CARD"); // should be blocked

        // Refund
        System.out.println("\n--- Refund ---");
        gateway.refund(1); // refund Alice's first UPI payment

        // Try refund on failed txn
        gateway.refund(4); // Charlie's failed txn

        gateway.showTransactions();
    }
}

