package paymentgateway;

/** Chain of Responsibility: each checker validates, then passes to next. */
public abstract class FraudChecker {
    private FraudChecker next;

    public FraudChecker setNext(FraudChecker next) {
        this.next = next;
        return next;
    }

    public boolean check(Transaction txn) {
        if (!doCheck(txn)) return false;
        if (next != null) return next.check(txn);
        return true;
    }

    protected abstract boolean doCheck(Transaction txn);
    public abstract String getName();
}

