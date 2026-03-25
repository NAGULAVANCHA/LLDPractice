package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TOKEN BUCKET Algorithm:
 * - Each client has a bucket of tokens
 * - Tokens refill at a constant rate
 * - Each request costs 1 token
 * - Allows bursts up to bucket capacity
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final int maxTokens;       // bucket capacity
    private final double refillRate;   // tokens per second

    private static class Bucket {
        double tokens;
        long lastRefillTime;

        Bucket(double tokens) {
            this.tokens = tokens;
            this.lastRefillTime = System.nanoTime();
        }
    }

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(int maxTokens, double refillRatePerSecond) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRatePerSecond;
    }

    @Override
    public synchronized boolean allowRequest(String clientId) {
        Bucket bucket = buckets.computeIfAbsent(clientId, k -> new Bucket(maxTokens));

        // Refill tokens based on elapsed time
        long now = System.nanoTime();
        double elapsed = (now - bucket.lastRefillTime) / 1_000_000_000.0;
        bucket.tokens = Math.min(maxTokens, bucket.tokens + elapsed * refillRate);
        bucket.lastRefillTime = now;

        if (bucket.tokens >= 1.0) {
            bucket.tokens -= 1.0;
            return true;
        }
        return false;
    }

    @Override
    public String getAlgorithmName() { return "Token Bucket"; }
}

