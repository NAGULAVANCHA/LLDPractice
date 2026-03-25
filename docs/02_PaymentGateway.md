# Payment Gateway / UPI Switch — Complete Guide

---

## Part 1: Understanding the Problem

A payment gateway that supports multiple payment methods (UPI, Card, NetBanking), validates transactions through fraud checks, and manages payment lifecycle.

### Requirements
- ✓ Multiple payment methods via Strategy Pattern
- ✓ Fraud detection chain (amount limits, velocity checks)
- ✓ Transaction state: INITIATED → PROCESSING → SUCCESS/FAILED → REFUNDED
- ✓ Refund support with status validation
- ✓ Thread-safe concurrent payments

---

## Part 2: Three Patterns Combined

### 1. Strategy — Payment Methods
```java
interface PaymentMethod {
    boolean processPayment(double amount, String txnId);
    boolean refund(String txnId, double amount);
}

class UPIPayment implements PaymentMethod { /* UPI-specific logic */ }
class CardPayment implements PaymentMethod { /* Card auth logic */ }
class NetBankingPayment implements PaymentMethod { /* Bank redirect */ }
```
**Adding Wallet, Crypto, BNPL** = new class, zero changes to gateway.

### 2. Chain of Responsibility — Fraud Checks
```
Transaction(₹15,000)
    ↓
AmountLimitChecker (max ₹10,000)
  → ₹15,000 > ₹10,000? YES → REJECTED ❌

Transaction(₹500) — 4th txn in 1 minute
    ↓
AmountLimitChecker → ₹500 ≤ ₹10,000 → ✓ PASS
    ↓
VelocityChecker (max 3/min)
  → 4 > 3? YES → REJECTED ❌
```

### 3. State — Transaction Lifecycle
```
INITIATED → PROCESSING → SUCCESS → REFUNDED
                      ↘ FAILED
```

---

## Part 3: Data Flow

```
1. gateway.processPayment("alice", 500, "UPI")
   → Create Transaction(alice, 500, UPI) → INITIATED
   → AmountLimitChecker: 500 ≤ 10000 → ✓
   → VelocityChecker: 1/3 → ✓
   → UPI.processPayment(500) → random 90% success
   → Status: SUCCESS

2. gateway.refund(txnId=1)
   → Check status == SUCCESS → ✓
   → UPI.refund(500) → ✓
   → Status: REFUNDED
```

---

## Part 4: Follow-Up Questions

| Question | Answer |
|---|---|
| Idempotency? | Use idempotency key per request. Store in a Set. Reject duplicates. |
| Retry on timeout? | Exponential backoff. Check transaction status before retrying to avoid double charge. |
| Webhook notifications? | Observer pattern: notify merchant on status change (SUCCESS, FAILED, REFUNDED). |
| Multi-currency? | Add `Currency` field to Transaction. PaymentMethod handles conversion. |

---

📁 **Source code:** `src/paymentgateway/`

