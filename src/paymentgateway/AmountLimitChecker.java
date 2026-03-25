package paymentgateway;

public class AmountLimitChecker extends FraudChecker {
    private final double maxAmount;

    public AmountLimitChecker(double maxAmount) { this.maxAmount = maxAmount; }

    @Override
    protected boolean doCheck(Transaction txn) {
        if (txn.getAmount() > maxAmount) {
            System.out.println("  ⚠️ FRAUD CHECK: Amount ₹" + txn.getAmount() + " exceeds limit ₹" + maxAmount);
            return false;
        }
        System.out.println("  ✓ Amount limit check passed");
        return true;
    }

    @Override public String getName() { return "AmountLimit"; }
}

