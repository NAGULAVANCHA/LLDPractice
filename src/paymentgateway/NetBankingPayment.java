package paymentgateway;

public class NetBankingPayment implements PaymentMethod {
    @Override
    public boolean processPayment(double amount, String txnId) {
        System.out.println("  [NETBANKING] Redirecting to bank portal...");
        System.out.println("  [NETBANKING] ✅ Payment authorized");
        return true; // 100% success for demo
    }

    @Override
    public boolean refund(String txnId, double amount) {
        System.out.println("  [NETBANKING] ✅ Refund processed");
        return true;
    }

    @Override public String getName() { return "NETBANKING"; }
}

