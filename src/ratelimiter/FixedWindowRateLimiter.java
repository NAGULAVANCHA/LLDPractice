package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FIXED WINDOW COUNTER Algorithm:
 * - Time divided into fixed windows (e.g., every second)
 * - Count requests per window
 * - Reset at window boundary
 * - Simple but has boundary burst problem
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeMs;

    private static class WindowCounter {
        long windowStart;
        int count;

        WindowCounter(long windowStart) {
            this.windowStart = windowStart;
            this.count = 0;
        }
    }

    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        WindowCounter counter = counters.computeIfAbsent(clientId, k -> new WindowCounter(now));

        // Check if we've moved to a new window
        if (now - counter.windowStart >= windowSizeMs) {
            counter.windowStart = now;
            counter.count = 0;
        }

        if (counter.count < maxRequests) {
            counter.count++;
            return true;
        }
        return false;
    }

    @Override
    public String getAlgorithmName() { return "Fixed Window Counter"; }
}

