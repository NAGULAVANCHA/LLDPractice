package paymentgateway;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VelocityChecker extends FraudChecker {
    private final int maxTxnPerMinute;
    private final Map<String, List<Long>> userTimestamps = new ConcurrentHashMap<>();

    public VelocityChecker(int maxTxnPerMinute) { this.maxTxnPerMinute = maxTxnPerMinute; }

    @Override
    protected boolean doCheck(Transaction txn) {
        long now = System.currentTimeMillis();
        List<Long> timestamps = userTimestamps.computeIfAbsent(txn.getUserId(), k -> new ArrayList<>());

        // Remove timestamps older than 60 seconds
        timestamps.removeIf(t -> now - t > 60_000);

        if (timestamps.size() >= maxTxnPerMinute) {
            System.out.println("  ⚠️ FRAUD CHECK: User " + txn.getUserId() +
                    " exceeded " + maxTxnPerMinute + " txns/min");
            return false;
        }
        timestamps.add(now);
        System.out.println("  ✓ Velocity check passed (" + timestamps.size() + "/" + maxTxnPerMinute + ")");
        return true;
    }

    @Override public String getName() { return "Velocity"; }
}

