# Problem 18: ATM Machine — Complete Guide

---

## Part 1: Understanding the Problem

An ATM that handles card insertion, PIN authentication, balance inquiries, withdrawals, and deposits.

### Requirements
- ✓ State-based flow: IDLE → CARD_INSERTED → AUTHENTICATED
- ✓ PIN validation with auto-eject on wrong PIN
- ✓ Cash dispenser with denomination chain ($100 → $50 → $20 → $10)
- ✓ Balance check, withdraw (with stock check), deposit
- ✓ Rollback if ATM can't physically dispense

---

## Part 2: Two Patterns Combined

### 1. State Pattern — ATM Behavior
```
IDLE ──insertCard()──→ CARD_INSERTED ──enterPin()──→ AUTHENTICATED
  ↑                        │ (wrong pin)                  │
  └────────────ejectCard()─┘                    checkBalance()
                                                withdraw()
                                                deposit()
                                                ejectCard() → IDLE
```

In IDLE: only `insertCard()` works. In CARD_INSERTED: only `enterPin()`. In AUTHENTICATED: checkBalance, withdraw, deposit all work.

### 2. Chain of Responsibility — Cash Dispenser
```
$100 handler → $50 handler → $20 handler → $10 handler

Withdraw $280:
  $100 handler: 2 bills ($200) → pass remainder $80
  $50 handler:  1 bill ($50)   → pass remainder $30
  $20 handler:  1 bill ($20)   → pass remainder $10
  $10 handler:  1 bill ($10)   → remainder $0 ✓

Result: 2×$100 + 1×$50 + 1×$20 + 1×$10 = $280
```

---

## Part 3: The Code — Explained

### CashDispenser — Chain of Responsibility
```java
public class CashDispenser {
    private final int denomination;
    private int count;              // bills available
    private CashDispenser next;     // next denomination in chain

    public CashDispenser setNext(CashDispenser next) {
        this.next = next;
        return next;                 // fluent chaining
    }

    public boolean dispense(int amount) {
        int billsNeeded = amount / denomination;
        int billsUsed = Math.min(billsNeeded, count);  // can't use more than we have
        int remainder = amount - billsUsed * denomination;

        if (billsUsed > 0) {
            count -= billsUsed;
            System.out.println("Dispensing " + billsUsed + " x $" + denomination);
        }

        if (remainder > 0) {
            if (next != null) return next.dispense(remainder);  // pass to next
            else {
                count += billsUsed;   // ROLLBACK — can't complete
                return false;
            }
        }
        return true;  // fully dispensed
    }
}
```

### Setting Up the Chain
```java
CashDispenser d100 = new CashDispenser(100, 10);  // 10 bills of $100
CashDispenser d50  = new CashDispenser(50, 10);
CashDispenser d20  = new CashDispenser(20, 10);
CashDispenser d10  = new CashDispenser(10, 10);
d100.setNext(d50).setNext(d20).setNext(d10);  // fluent chain
```

### ATM — State Machine + Rollback
```java
public class ATM {
    private enum State { IDLE, CARD_INSERTED, AUTHENTICATED }
    private State state;
    private Account currentAccount;

    public void withdraw(int amount) {
        if (state != State.AUTHENTICATED) { "Not authenticated!"; return; }

        // Step 1: Debit account
        if (!currentAccount.withdraw(amount)) { "Insufficient balance!"; return; }

        // Step 2: Try to dispense cash
        if (!dispenser.dispense(amount)) {
            currentAccount.deposit(amount);  // ROLLBACK — can't dispense
            "ATM cannot dispense. Reversed.";
        } else {
            "Withdrawal complete!";
        }
    }
}
```

**Rollback pattern:** Debit first, attempt physical dispense, reverse debit on failure. Simple transaction safety without a database.

---

## Part 4: Data Flow — Withdrawal

```
1. atm.insertCard("ACC-001")
   → state: IDLE → CARD_INSERTED
   → currentAccount = accounts["ACC-001"]

2. atm.enterPin("1234")
   → account.validatePin("1234") → true
   → state: CARD_INSERTED → AUTHENTICATED

3. atm.withdraw(280)
   → state == AUTHENTICATED ✓
   → account.withdraw(280) → balance: 5000 → 4720 ✓
   → dispenser.dispense(280):
     $100: 2 bills → remainder 80
     $50:  1 bill  → remainder 30
     $20:  1 bill  → remainder 10
     $10:  1 bill  → remainder 0 ✓
   → "Withdrawal complete! Balance: $4720"

4. atm.ejectCard()
   → state: AUTHENTICATED → IDLE
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| What if ATM is out of a denomination? | The chain skips it. If it can't fulfill the total, rollback everything. |
| PIN retry limit? | Add `int attempts` counter. After 3 wrong PINs, block card. |
| Transaction log? | Create `Transaction` class (type, amount, timestamp). ATM stores a `List<Transaction>`. |
| Concurrent ATM users? | Each ATM is single-user by design (one card slot). Multiple ATMs share account data — need `synchronized` on Account. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **State** | ATM behavior changes: IDLE → CARD_INSERTED → AUTHENTICATED |
| **Chain of Responsibility** | Cash dispenser: $100 → $50 → $20 → $10 |
| **Rollback** | Reverse account debit if physical dispense fails |
| **Fluent Chaining** | `d100.setNext(d50).setNext(d20).setNext(d10)` |
| **SRP** | ATM manages flow, Account manages balance, Dispenser manages cash |

---

📁 **Source code:** `src/atm/`
