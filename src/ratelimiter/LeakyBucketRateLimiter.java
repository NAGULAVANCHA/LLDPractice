package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LEAKY BUCKET Algorithm:
 * - Requests go into a queue (bucket) of fixed size
 * - Requests are "leaked" (processed) at a constant rate
 * - If bucket is full, reject
 * - Smooths out bursty traffic
 */
public class LeakyBucketRateLimiter implements RateLimiter {

    private final int bucketSize;
    private final Map<String, LinkedBlockingQueue<Long>> buckets = new ConcurrentHashMap<>();

    public LeakyBucketRateLimiter(int bucketSize) {
        this.bucketSize = bucketSize;
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        LinkedBlockingQueue<Long> bucket = buckets.computeIfAbsent(
                clientId, k -> new LinkedBlockingQueue<>(bucketSize));

        if (bucket.offer(System.currentTimeMillis())) {
            return true; // added to bucket
        }
        return false; // bucket full
    }

    /**
     * Simulate "leaking" — process one request from the bucket.
     */
    public void leak(String clientId) {
        LinkedBlockingQueue<Long> bucket = buckets.get(clientId);
        if (bucket != null) {
            bucket.poll();
        }
    }

    @Override
    public String getAlgorithmName() { return "Leaky Bucket"; }
}

