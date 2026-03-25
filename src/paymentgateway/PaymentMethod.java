package paymentgateway;

/** Strategy Pattern: different payment processing algorithms. */
public interface PaymentMethod {
    boolean processPayment(double amount, String transactionId);
    boolean refund(String transactionId, double amount);
    String getName();
}

