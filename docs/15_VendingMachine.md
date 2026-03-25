# Problem 2: Vending Machine — Complete Guide

---

## Part 1: How to Approach This Problem

### Step 1: Clarify Requirements
- What products does the machine sell?
- Can user insert money in parts (e.g., $1 then $1 more)?
- What happens if insufficient funds?
- Can the user cancel mid-transaction?
- Do we need change calculation?
- Do we need inventory tracking (out of stock)?

**Assumptions:**
- ✓ Machine holds multiple products with quantities
- ✓ User inserts money, selects product, gets item + change
- ✓ User can cancel and get refund
- ✓ Machine tracks inventory and rejects out-of-stock items

### Step 2: Identify Core Entities

| Noun → Class |
|---|
| Product (name, price) |
| ItemSlot (product + quantity) |
| VendingMachine (inventory, balance, state) |
| VendingMachineState (interface for state behavior) |
| IdleState, HasMoneyState, DispensingState |

### Step 3: Identify the Key Insight — **It's a State Machine!**

The vending machine behaves **differently** depending on what state it's in:

```
┌──────────┐   insertMoney()   ┌────────────┐  selectProduct()  ┌─────────────┐
│   IDLE   │ ───────────────── │  HAS_MONEY │ ──────────────── │ DISPENSING  │
│          │                   │            │                   │             │
│ "Insert  │                   │ "Select a  │                   │ "Here's     │
│  money!" │                   │  product"  │                   │  your item" │
└──────────┘                   └────────────┘                   └──────┬──────┘
     ▲                              │                                  │
     │                         cancelTransaction()                     │
     │                              │                            dispense()
     │                              ▼                                  │
     │                        (refund money)                           │
     └─────────────────────────────────────────────────────────────────┘
```

Without the State Pattern, you'd have messy if/else everywhere:
```java
// BAD — without State Pattern
void insertMoney(double amount) {
    if (state == "IDLE") { ... }
    else if (state == "HAS_MONEY") { ... }
    else if (state == "DISPENSING") { ... }
}
void selectProduct(String code) {
    if (state == "IDLE") { ... }
    else if (state == "HAS_MONEY") { ... }
    else if (state == "DISPENSING") { ... }
}
// Every method has the same if/else chain!
```

---

## 🎯 Design Pattern: State Pattern

### Problem It Solves
An object's behavior changes based on its **internal state**, and you want to avoid massive `if/else` or `switch` blocks.

### Real World Analogy
Think of a **traffic light**:
- **RED state**: Cars stop, pedestrians walk
- **YELLOW state**: Cars slow down
- **GREEN state**: Cars go, pedestrians wait

The traffic light is the **same object**, but it **behaves differently** in each state. The actions (what cars/pedestrians do) depend on the current state.

### How It Works

| Step | What to Do |
|---|---|
| 1. Define a **State interface** | Lists ALL actions the object can perform |
| 2. Create **concrete states** | Each state implements the interface differently |
| 3. The main object **delegates** to current state | `state.insertMoney(this, amount)` |
| 4. States **transition** to other states | `machine.setState(new HasMoneyState())` |

### WITHOUT State Pattern ❌
```java
class VendingMachine {
    String state = "IDLE";
    
    void insertMoney(double amount) {
        if (state.equals("IDLE")) {
            balance += amount;
            state = "HAS_MONEY";
        } else if (state.equals("HAS_MONEY")) {
            balance += amount;
        } else if (state.equals("DISPENSING")) {
            System.out.println("Wait!");
        }
    }
    
    void selectProduct(String code) {
        if (state.equals("IDLE")) {
            System.out.println("Insert money first!");
        } else if (state.equals("HAS_MONEY")) {
            // 30 lines of validation + dispensing logic
        } else if (state.equals("DISPENSING")) {
            System.out.println("Wait!");
        }
    }
    // Every method is a HUGE switch/if-else on state
}
```
- ❌ Methods get HUGE as you add states
- ❌ Adding a new state means modifying EVERY method
- ❌ Hard to understand what the machine does in any one state

### WITH State Pattern ✅
```java
interface VendingMachineState {
    void insertMoney(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, String code);
    void dispense(VendingMachine machine);
    void cancelTransaction(VendingMachine machine);
}

class IdleState implements VendingMachineState {
    void insertMoney(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        machine.setState(new HasMoneyState());  // TRANSITION
    }
    void selectProduct(...) { print("Insert money first!"); }
    void dispense(...)      { print("Insert money first!"); }
    void cancelTransaction(...) { print("Nothing to cancel"); }
}
```
- ✅ Each state is its own class with **clear, focused** behavior
- ✅ Adding a new state = adding a **new class** (OCP)
- ✅ Easy to see ALL behaviors for one state at a glance

---

## Part 2: The Code — Explained

### State Interface
```java
public interface VendingMachineState {
    void insertMoney(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, String code);
    void dispense(VendingMachine machine);
    void cancelTransaction(VendingMachine machine);
}
```

**Why pass `VendingMachine machine`?**
- States need to **modify** the machine (change balance, change state)
- The state doesn't OWN the machine; it's a helper that receives it
- This is the standard State Pattern approach

### IdleState — Waiting for money
```java
public class IdleState implements VendingMachineState {
    public void insertMoney(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        machine.setState(new HasMoneyState());  // → transition!
    }
    public void selectProduct(...) { "Please insert money first!"; }
    public void dispense(...)      { "Please insert money first!"; }
    public void cancelTransaction(...) { "No transaction to cancel."; }
}
```

**Only `insertMoney()` does real work** — all other actions are invalid in this state.

### HasMoneyState — Money inserted, waiting for selection
```java
public class HasMoneyState implements VendingMachineState {
    public void insertMoney(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        // Stay in HasMoneyState — can keep adding money
    }
    
    public void selectProduct(VendingMachine machine, String code) {
        // Validate: does product exist? In stock? Enough balance?
        ItemSlot slot = machine.getSlot(code);
        if (slot == null) { "Invalid code"; return; }
        if (!slot.isAvailable()) { "Out of stock!"; return; }
        if (machine.getBalance() < slot.getProduct().getPrice()) {
            "Insufficient balance!"; return;
        }
        machine.setSelectedSlot(slot);
        machine.setState(new DispensingState());  // → transition!
        machine.dispense();  // auto-trigger dispensing
    }
    
    public void cancelTransaction(VendingMachine machine) {
        // Return all money, go back to IDLE
        machine.setBalance(0);
        machine.setState(new IdleState());  // → transition!
    }
}
```

### DispensingState — Product selected, dispensing
```java
public class DispensingState implements VendingMachineState {
    public void insertMoney(...) { "Wait, dispensing..."; }
    public void selectProduct(...) { "Wait, dispensing..."; }
    
    public void dispense(VendingMachine machine) {
        ItemSlot slot = machine.getSelectedSlot();
        slot.dispense();  // decrement quantity
        double change = machine.getBalance() - slot.getProduct().getPrice();
        if (change > 0) print("Change: $" + change);
        
        // Reset everything
        machine.setBalance(0);
        machine.setSelectedSlot(null);
        machine.setState(new IdleState());  // → back to idle
    }
    
    public void cancelTransaction(...) { "Cannot cancel, dispensing!"; }
}
```

### VendingMachine — Context (delegates to state)
```java
public class VendingMachine {
    private VendingMachineState state;
    private double balance;
    private ItemSlot selectedSlot;
    private final Map<String, ItemSlot> inventory;

    public VendingMachine() {
        this.state = new IdleState();  // start idle
        this.inventory = new HashMap<>();
    }

    // Delegate ALL actions to current state
    public void insertMoney(double amount) { state.insertMoney(this, amount); }
    public void selectProduct(String code) { state.selectProduct(this, code); }
    public void dispense()                 { state.dispense(this); }
    public void cancelTransaction()        { state.cancelTransaction(this); }
}
```

**The beauty:** VendingMachine's methods are ONE LINE each! All logic is in the states.

### Product & ItemSlot

```java
public class Product {
    private final String name;
    private final double price;
}

public class ItemSlot {
    private final Product product;
    private int quantity;
    
    public boolean isAvailable() { return quantity > 0; }
    public void dispense() { quantity--; }
    public void restock(int amount) { quantity += amount; }
}
```

---

## Part 3: Data Flow — Buying a Coke

```
1. Machine starts in IDLE state

2. User: insertMoney(2.00)
   → Machine delegates: idleState.insertMoney(machine, 2.00)
   → IdleState: balance = 2.00, setState(HasMoneyState)
   → Machine is now in HAS_MONEY state

3. User: selectProduct("A1")  (Coke, $1.50)
   → Machine delegates: hasMoneyState.selectProduct(machine, "A1")
   → HasMoneyState: 
     - slot exists? YES
     - in stock? YES  
     - balance ($2.00) >= price ($1.50)? YES
     - setState(DispensingState)
     - machine.dispense()

4. Auto-dispense triggered:
   → Machine delegates: dispensingState.dispense(machine)
   → DispensingState:
     - slot.dispense() → quantity 5→4
     - change = $2.00 - $1.50 = $0.50
     - Print: "Dispensed: Coke", "Change: $0.50"
     - Reset: balance=0, selectedSlot=null
     - setState(IdleState) → back to IDLE

5. Machine is back in IDLE, ready for next customer
```

---

## Part 4: Interview Script

1. *"This is a classic State Pattern problem because the machine behaves differently based on its state."*
2. *"I'll identify the states: Idle → HasMoney → Dispensing → Idle"*
3. *"Each state handles the same set of actions (insert money, select, dispense, cancel) differently"*
4. *"I'll create a VendingMachineState interface and concrete state classes"*
5. *"The VendingMachine delegates all actions to the current state"*

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| How to add a new state (e.g., MaintenanceState)? | Create `MaintenanceState implements VendingMachineState`. All methods print "Out of service". No existing code changes. |
| How to handle multiple currencies? | Create a `CurrencyConverter` to normalize all inputs to one base currency. |
| What about card payments? | Add `PaymentStrategy` interface with `CashPayment`, `CardPayment`. Strategy Pattern again. |
| Thread safety for concurrent users? | Each physical machine serves one user at a time (by design). No concurrency issue for single machine. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **State** | VendingMachineState — behavior changes with state |
| **SRP** | Each state class handles ONE state's behavior |
| **OCP** | Add new states without modifying existing ones |
| **Encapsulation** | balance, inventory are private |
| **Delegation** | VendingMachine delegates to state |

---

📁 **Source code:** `src/vendingmachine/`

