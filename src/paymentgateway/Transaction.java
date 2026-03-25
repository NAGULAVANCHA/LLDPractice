package paymentgateway;

public class Transaction {
    private static int counter = 0;
    private final int transactionId;
    private final String userId;
    private final double amount;
    private final PaymentMethod method;
    private PaymentStatus status;
    private final long timestamp;

    public Transaction(String userId, double amount, PaymentMethod method) {
        this.transactionId = ++counter;
        this.userId = userId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.INITIATED;
        this.timestamp = System.currentTimeMillis();
    }

    public int getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Txn#" + transactionId + " | " + userId + " | ₹" +
                String.format("%.2f", amount) + " | " + method.getName() + " | " + status;
    }
}

