package splitwise;

/**
 * Represents one user's share in an expense.
 */
public class Split {
    private final SplitwiseUser user;
    private double amount; // the amount this user owes

    public Split(SplitwiseUser user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public Split(SplitwiseUser user) {
        this(user, 0); // for EQUAL split, amount set later
    }

    public SplitwiseUser getUser() { return user; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}

