package splitwise;

/**
 * SPLITWISE LLD
 * ==============
 * Key Concepts:
 *  - Strategy Pattern: Different ways to split (Equal, Exact, Percentage)
 *  - SRP: Expense, User, Group, BalanceSheet are all separate concerns
 *  - OCP: Add new split strategies without modifying Expense
 *
 * Classes:
 *  User         -> A person with name and ID
 *  SplitType    -> Enum: EQUAL, EXACT, PERCENTAGE
 *  Split        -> How much each user owes in a single expense
 *  Expense      -> A bill paid by one person, split among many
 *  Group        -> A group of users sharing expenses
 *  BalanceSheet -> Tracks who owes whom (net balances)
 *  SplitwiseService -> Main service managing groups and settlements
 *
 * Flow:
 *  1. Create users and a group
 *  2. Add expenses (payer + split among members)
 *  3. BalanceSheet calculates net amounts
 *  4. Show simplified debts (minimize transactions)
 */
public class SplitwiseUser {
    private final String userId;
    private final String name;
    private final String email;

    public SplitwiseUser(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name; }

    @Override
    public int hashCode() { return userId.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SplitwiseUser)) return false;
        return userId.equals(((SplitwiseUser) o).userId);
    }
}

