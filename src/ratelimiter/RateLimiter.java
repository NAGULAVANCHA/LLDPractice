package ratelimiter;

/**
 * RATE LIMITER LLD
 * =================
 * Key Concepts:
 *  - Strategy Pattern: Multiple algorithms (Token Bucket, Sliding Window, Fixed Window, Leaky Bucket)
 *  - Template Method:  Common interface, different implementations
 *  - Thread Safety:    synchronized / atomic operations
 *
 * Algorithms Implemented:
 *
 *  1. TOKEN BUCKET:
 *     - Bucket holds tokens (max = capacity)
 *     - Tokens are added at a fixed rate (refill)
 *     - Each request consumes 1 token
 *     - If no tokens left -> reject
 *     - Pros: Allows bursts up to capacity
 *
 *  2. SLIDING WINDOW LOG:
 *     - Keep a log of all request timestamps
 *     - For each new request, remove old entries outside the window
 *     - If count < limit -> allow
 *     - Pros: Very accurate, no boundary issues
 *     - Cons: Memory grows with request count
 *
 *  3. FIXED WINDOW COUNTER:
 *     - Divide time into fixed windows (e.g., 1-second windows)
 *     - Count requests in current window
 *     - Reset counter at window boundary
 *     - Pros: Simple, low memory
 *     - Cons: Boundary spike problem (2x burst at window edges)
 *
 *  4. LEAKY BUCKET:
 *     - Requests enter a queue (bucket) of fixed size
 *     - Requests are processed at a constant rate
 *     - If bucket is full -> reject
 *     - Pros: Smooths out bursts, constant output rate
 */
public interface RateLimiter {
    boolean allowRequest(String clientId);
    String getAlgorithmName();
}

