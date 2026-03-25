package splitwise;

import java.util.List;

public class Expense {
    private final String description;
    private final double totalAmount;
    private final SplitwiseUser paidBy;
    private final List<Split> splits;
    private final SplitType splitType;

    public Expense(String description, double totalAmount, SplitwiseUser paidBy,
                   List<Split> splits, SplitType splitType) {
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.splitType = splitType;

        // Compute splits based on type
        switch (splitType) {
            case EQUAL -> computeEqualSplit();
            case PERCENTAGE -> computePercentageSplit();
            case EXACT -> validateExactSplit();
        }
    }

    private void computeEqualSplit() {
        double share = totalAmount / splits.size();
        for (Split s : splits) {
            s.setAmount(share);
        }
    }

    private void computePercentageSplit() {
        // In PERCENTAGE mode, split.amount initially holds the percentage
        double totalPct = splits.stream().mapToDouble(Split::getAmount).sum();
        if (Math.abs(totalPct - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100, got " + totalPct);
        }
        for (Split s : splits) {
            s.setAmount(totalAmount * s.getAmount() / 100.0);
        }
    }

    private void validateExactSplit() {
        double total = splits.stream().mapToDouble(Split::getAmount).sum();
        if (Math.abs(total - totalAmount) > 0.01) {
            throw new IllegalArgumentException(
                    "Exact splits must sum to " + totalAmount + ", got " + total);
        }
    }

    public String getDescription() { return description; }
    public double getTotalAmount() { return totalAmount; }
    public SplitwiseUser getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }

    @Override
    public String toString() {
        return description + " ($" + String.format("%.2f", totalAmount) + " paid by " + paidBy + ")";
    }
}

