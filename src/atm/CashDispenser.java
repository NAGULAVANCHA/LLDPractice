package atm;

/**
 * Chain of Responsibility: Each denomination handler tries to
 * dispense as many bills as possible, then passes remainder to next.
 */
public class CashDispenser {
    private final int denomination;
    private int count; // number of bills available
    private CashDispenser next;

    public CashDispenser(int denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }

    public CashDispenser setNext(CashDispenser next) {
        this.next = next;
        return next;
    }

    public boolean dispense(int amount) {
        int billsNeeded = amount / denomination;
        int billsUsed = Math.min(billsNeeded, count);
        int remainder = amount - billsUsed * denomination;

        if (billsUsed > 0) {
            count -= billsUsed;
            System.out.println("  Dispensing " + billsUsed + " x $" + denomination);
        }

        if (remainder > 0) {
            if (next != null) {
                return next.dispense(remainder);
            } else {
                System.out.println("  ❌ Cannot dispense $" + remainder + " (insufficient denominations)");
                // Rollback
                count += billsUsed;
                return false;
            }
        }
        return true;
    }

    public void displayStock() {
        System.out.println("  $" + denomination + " x " + count + " = $" + (denomination * count));
        if (next != null) next.displayStock();
    }
}

