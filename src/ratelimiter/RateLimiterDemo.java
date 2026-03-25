package ratelimiter;

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Demo ===\n");

        // --- 1. Token Bucket ---
        System.out.println("--- Token Bucket (capacity=5, refill=2/sec) ---");
        RateLimiter tokenBucket = new TokenBucketRateLimiter(5, 2.0);
        for (int i = 1; i <= 8; i++) {
            boolean allowed = tokenBucket.allowRequest("user1");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }
        System.out.println("  (waiting 2 seconds for refill...)");
        Thread.sleep(2000);
        for (int i = 9; i <= 12; i++) {
            boolean allowed = tokenBucket.allowRequest("user1");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }

        // --- 2. Sliding Window Log ---
        System.out.println("\n--- Sliding Window Log (3 req per 1000ms) ---");
        RateLimiter slidingWindow = new SlidingWindowLogRateLimiter(3, 1000);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = slidingWindow.allowRequest("user2");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }
        System.out.println("  (waiting 1.1 seconds...)");
        Thread.sleep(1100);
        for (int i = 6; i <= 8; i++) {
            boolean allowed = slidingWindow.allowRequest("user2");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }

        // --- 3. Fixed Window Counter ---
        System.out.println("\n--- Fixed Window Counter (3 req per 1000ms) ---");
        RateLimiter fixedWindow = new FixedWindowRateLimiter(3, 1000);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = fixedWindow.allowRequest("user3");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }

        // --- 4. Leaky Bucket ---
        System.out.println("\n--- Leaky Bucket (bucket size=3) ---");
        LeakyBucketRateLimiter leakyBucket = new LeakyBucketRateLimiter(3);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = leakyBucket.allowRequest("user4");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }
        System.out.println("  (leaking 2 requests...)");
        leakyBucket.leak("user4");
        leakyBucket.leak("user4");
        for (int i = 6; i <= 8; i++) {
            boolean allowed = leakyBucket.allowRequest("user4");
            System.out.println("  Request " + i + ": " + (allowed ? "ALLOWED ✓" : "REJECTED ✗"));
        }
    }
}

