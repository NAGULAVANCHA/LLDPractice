# Problem 6: Splitwise — Complete Guide

---

## Part 1: Understanding the Problem

### What is Splitwise?
An app to split expenses among friends. Core features:
- A group of friends share expenses
- One person pays, the bill is split among some/all members
- The system tracks **who owes whom** and **how much**
- Supports different split strategies: Equal, Exact, Percentage

### Requirements
- ✓ Users can create expenses
- ✓ One user pays, split among multiple users
- ✓ Three split types: EQUAL, EXACT, PERCENTAGE
- ✓ Track net balances (who owes whom)
- ✓ Minimize transactions for settlement

---

## Part 2: Identify Core Entities

| Class | Responsibility |
|---|---|
| `SplitwiseUser` | Person with id, name, email |
| `SplitType` | Enum: EQUAL, EXACT, PERCENTAGE |
| `Split` | One user's share in an expense |
| `Expense` | A bill: who paid, total amount, how to split |
| `BalanceSheet` | Tracks net balances between all pairs of users |

---

## Part 3: The Split Strategies

### EQUAL Split
```
Alice pays $100 dinner. Split among Alice, Bob, Charlie, Dave.
Each person owes: $100 / 4 = $25
→ Bob owes Alice $25
→ Charlie owes Alice $25
→ Dave owes Alice $25
(Alice owes herself $25, which cancels out)
```

### EXACT Split
```
Bob pays $60 cab. Split: Alice=$20, Bob=$10, Charlie=$30
→ Alice owes Bob $20
→ Charlie owes Bob $30
(Bob owes himself $10, cancels out)
Validation: 20 + 10 + 30 = 60 ✓
```

### PERCENTAGE Split
```
Charlie pays $200 hotel. Split: 25% each to Alice, Bob, Charlie, Dave
→ Alice owes Charlie $50 (25% of $200)
→ Bob owes Charlie $50
→ Dave owes Charlie $50
Validation: 25 + 25 + 25 + 25 = 100% ✓
```

---

## Part 4: The Code — Explained

### SplitType (Enum)
```java
public enum SplitType {
    EQUAL,
    EXACT,
    PERCENTAGE
}
```

### Split — One User's Share
```java
public class Split {
    private final SplitwiseUser user;
    private double amount;  // the amount this user owes

    public Split(SplitwiseUser user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public Split(SplitwiseUser user) {
        this(user, 0);  // for EQUAL split, amount computed later
    }
}
```

**Two constructors:**
- `Split(user, amount)` — for EXACT/PERCENTAGE where you specify the amount upfront
- `Split(user)` — for EQUAL where the system calculates it

### Expense — The Bill
```java
public class Expense {
    private final String description;
    private final double totalAmount;
    private final SplitwiseUser paidBy;
    private final List<Split> splits;
    private final SplitType splitType;

    public Expense(String description, double totalAmount, SplitwiseUser paidBy,
                   List<Split> splits, SplitType splitType) {
        // ... store fields ...
        
        switch (splitType) {
            case EQUAL      -> computeEqualSplit();
            case PERCENTAGE -> computePercentageSplit();
            case EXACT      -> validateExactSplit();
        }
    }

    private void computeEqualSplit() {
        double share = totalAmount / splits.size();
        for (Split s : splits) {
            s.setAmount(share);  // set each person's share
        }
    }

    private void computePercentageSplit() {
        // splits initially hold percentages, not dollar amounts
        double totalPct = splits.stream().mapToDouble(Split::getAmount).sum();
        if (Math.abs(totalPct - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100!");
        }
        for (Split s : splits) {
            s.setAmount(totalAmount * s.getAmount() / 100.0);
        }
    }

    private void validateExactSplit() {
        double total = splits.stream().mapToDouble(Split::getAmount).sum();
        if (Math.abs(total - totalAmount) > 0.01) {
            throw new IllegalArgumentException("Exact splits must sum to total!");
        }
    }
}
```

**Key insight:** The Expense constructor **normalizes** all split types into dollar amounts. After construction, every Split has a concrete dollar amount regardless of the original split type.

### BalanceSheet — Who Owes Whom

This is the most interesting class. It maintains a 2D map of debts:

```java
// balances[debtorId][creditorId] = amount debtor owes creditor
Map<String, Map<String, Double>> balances;
```

```java
public void addExpense(Expense expense) {
    SplitwiseUser payer = expense.getPaidBy();
    
    for (Split split : expense.getSplits()) {
        SplitwiseUser debtor = split.getUser();
        if (debtor.equals(payer)) continue;  // skip payer's own share
        
        // debtor owes payer
        addBalance(debtor.getUserId(), payer.getUserId(), split.getAmount());
    }
}

private void addBalance(String debtorId, String creditorId, double amount) {
    // Add to debtor→creditor
    balances.get(debtorId).merge(creditorId, amount, Double::sum);
    // Subtract from creditor→debtor (net settlement)
    balances.get(creditorId).merge(debtorId, -amount, Double::sum);
}
```

**Why track both directions?**
If Alice owes Bob $20, and later Bob owes Alice $15, the net is:
- Alice owes Bob $5 (20 - 15)
- Bob owes Alice -$5 (which means Bob is OWED $5)

### Minimize Transactions — The Greedy Algorithm

```
Before simplification:
  Alice owes Bob $25
  Alice owes Charlie $50
  Bob owes Charlie $30
  Dave owes Alice $25
  Dave owes Charlie $50
  → 5 transactions!

After simplification:
  Net: Alice = -$50 (owes $50 total)
       Bob = -$5 (owes $5 total)
       Charlie = +$130 (is owed $130)
       Dave = -$75 (owes $75 total)
  
  → Dave pays Charlie $75
  → Alice pays Charlie $50
  → Bob pays Charlie $5
  → Only 3 transactions!
```

```java
public void showSimplifiedDebts() {
    // 1. Calculate NET balance for each user
    Map<String, Double> netBalance = new HashMap<>();
    // positive = owes money, negative = is owed money
    
    // 2. Separate into debtors and creditors
    List<Entry> debtors = /* net > 0 */;
    List<Entry> creditors = /* net < 0 */;
    
    // 3. Greedy matching: pair up debtor with creditor
    int i = 0, j = 0;
    while (i < debtors.size() && j < creditors.size()) {
        double settle = Math.min(debtor.amount, creditor.amount);
        // debtor pays creditor $settle
        // reduce both by settle amount
        // move to next debtor/creditor when one is settled
    }
}
```

---

## Part 5: Data Flow — Adding an Expense

```
Expense: Alice pays $100 dinner, split equally among 4 people.

1. Create splits:
   Split(Alice, 0), Split(Bob, 0), Split(Charlie, 0), Split(Dave, 0)

2. computeEqualSplit():
   share = 100/4 = $25
   → Split(Alice, 25), Split(Bob, 25), Split(Charlie, 25), Split(Dave, 25)

3. addExpense():
   Payer = Alice
   → Skip Alice's own split
   → Bob owes Alice $25
   → Charlie owes Alice $25
   → Dave owes Alice $25

4. Balance sheet:
   balances[Bob][Alice] = 25
   balances[Charlie][Alice] = 25
   balances[Dave][Alice] = 25
   (and reverse entries: balances[Alice][Bob] = -25, etc.)
```

---

## Part 6: Follow-Up Questions

| Question | Answer |
|---|---|
| How to handle groups? | Create a `Group` class with `List<SplitwiseUser>`. Expenses belong to a group. Default split is among group members. |
| How to settle debts? | Create `settle(userId1, userId2, amount)` method that adjusts the balance sheet. |
| How to handle different currencies? | Add `Currency` to Expense. Use a `CurrencyConverter` service. Convert to base currency for calculations. |
| How to handle recurring expenses? | Create `RecurringExpense` with a schedule (weekly, monthly). A scheduler auto-creates Expenses. |
| What about the "minimum transactions" problem? | This is NP-hard in general! The greedy approach works well enough. Optimal solution requires graph theory. |

---

## Part 7: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Strategy** | SplitType: different split algorithms (Equal, Exact, Percentage) |
| **SRP** | Expense calculates splits, BalanceSheet tracks debts, User stores info |
| **OCP** | Add new SplitTypes (e.g., WEIGHT-based) without modifying existing code |
| **Encapsulation** | Balance calculations are internal to BalanceSheet |
| **Enum** | SplitType — type-safe split categories |

---

📁 **Source code:** `src/splitwise/`

