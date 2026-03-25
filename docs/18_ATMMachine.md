# Problem 18: ATM Machine

| Pattern | Why |
|---|---|
| **State** | ATM behavior changes: IDLE → CARD_INSERTED → AUTHENTICATED |
| **Chain of Responsibility** | CashDispenser tries denominations largest-first |

## State Flow
```
IDLE → insertCard() → CARD_INSERTED → enterPin() → AUTHENTICATED
                                           ↓ (wrong pin)
                                         IDLE (eject card)
```

In AUTHENTICATED state: checkBalance, withdraw, deposit all work.
In other states: those operations are rejected.

## CashDispenser — Greedy Denomination
```
ATM has: $100×10, $50×20, $20×30, $10×50

Withdraw $180:
  Try $100: 1 note → remaining $80
  Try $50:  1 note → remaining $30
  Try $20:  1 note → remaining $10
  Try $10:  1 note → remaining $0 ✓

Dispenses: 1×$100 + 1×$50 + 1×$20 + 1×$10 = $180
```

## Rollback Pattern
```java
void withdraw(int amount) {
    if (!account.withdraw(amount)) { "Insufficient balance"; return; }
    if (!dispenser.dispense(amount)) {
        account.deposit(amount);  // ROLLBACK!
        "ATM can't dispense, reversed.";
    }
}
```
Debit first, try dispense, rollback on failure. Simple transaction safety.

📁 `src/atm/`

