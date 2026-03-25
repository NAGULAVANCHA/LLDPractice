package ratelimiter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SLIDING WINDOW LOG Algorithm:
 * - Maintain a sorted list of timestamps per client
 * - On each request, prune old timestamps outside the window
 * - If count of timestamps < limit, allow
 * - Most accurate but uses more memory
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeMs;
    private final Map<String, LinkedList<Long>> requestLogs = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        LinkedList<Long> log = requestLogs.computeIfAbsent(clientId, k -> new LinkedList<>());

        // Remove timestamps outside the window
        while (!log.isEmpty() && now - log.peekFirst() > windowSizeMs) {
            log.pollFirst();
        }

        if (log.size() < maxRequests) {
            log.addLast(now);
            return true;
        }
        return false;
    }

    @Override
    public String getAlgorithmName() { return "Sliding Window Log"; }
}

