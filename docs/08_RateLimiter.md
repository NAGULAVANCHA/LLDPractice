# Problem 8: Rate Limiter — Complete Guide

---

## Part 1: Understanding the Problem

### What is a Rate Limiter?
A system that controls how many requests a client can make in a given time period. Used to prevent abuse, protect servers, and ensure fair usage.

### Real World Examples
- Twitter: 300 tweets per 3 hours
- GitHub API: 5000 requests per hour
- Login page: 5 attempts per minute

### Requirements
- ✓ Multiple algorithms (Token Bucket, Sliding Window, Fixed Window, Leaky Bucket)
- ✓ Per-client rate limiting
- ✓ Thread-safe
- ✓ O(1) decision time

---

## Part 2: The Four Algorithms

### Algorithm 1: Token Bucket ⭐ (Most Common in Interviews)

**Analogy:** A bucket that holds tokens. Tokens drip in at a constant rate. Each request costs one token. No tokens? Request denied!

```
Bucket: [●●●●●] capacity=5, refill=2/sec

Request 1: [●●●●○] → ALLOWED (4 tokens left)
Request 2: [●●●○○] → ALLOWED (3 tokens left)
Request 3: [●●○○○] → ALLOWED (2 tokens left)
Request 4: [●○○○○] → ALLOWED (1 token left)
Request 5: [○○○○○] → ALLOWED (0 tokens left)
Request 6: [○○○○○] → REJECTED! No tokens!

... 1 second passes, 2 tokens refill ...

Request 7: [●○○○○] → ALLOWED
```

**Pros:** Allows bursts (up to bucket capacity)
**Cons:** Burst might overwhelm backend

```java
class TokenBucketRateLimiter {
    int maxTokens;
    double refillRate;  // tokens per second
    Map<String, Bucket> buckets;
    
    boolean allowRequest(String clientId) {
        Bucket bucket = buckets.get(clientId);
        // Refill based on elapsed time
        double elapsed = (now - bucket.lastRefillTime) / 1e9;
        bucket.tokens = min(maxTokens, bucket.tokens + elapsed * refillRate);
        bucket.lastRefillTime = now;
        
        if (bucket.tokens >= 1.0) {
            bucket.tokens -= 1.0;
            return true;   // ALLOWED
        }
        return false;      // REJECTED
    }
}
```

### Algorithm 2: Fixed Window Counter

**Analogy:** Divide time into fixed windows (e.g., every second). Count requests per window. Reset at boundary.

```
Window 1 (0-1sec): |●●●| limit=3 → 3 allowed
Window 2 (1-2sec): |●●○| limit=3 → 2 allowed, 1 more available
Window 3 (2-3sec): |○○○| limit=3 → fresh window
```

**Pros:** Simple, low memory
**Cons:** BOUNDARY SPIKE problem!

```
Window 1:          ..........●●●|  (3 requests at end of window)
Window 2:          |●●●..........  (3 requests at start of window)
                              ↑
                   6 requests in 1 second! (2x the limit!)
```

```java
class FixedWindowRateLimiter {
    int maxRequests;
    long windowSizeMs;
    Map<String, WindowCounter> counters;
    
    boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        WindowCounter counter = counters.get(clientId);
        
        if (now - counter.windowStart >= windowSizeMs) {
            counter.windowStart = now;
            counter.count = 0;  // reset for new window
        }
        
        if (counter.count < maxRequests) {
            counter.count++;
            return true;
        }
        return false;
    }
}
```

### Algorithm 3: Sliding Window Log

**Analogy:** Keep a log of ALL request timestamps. For each new request, remove old entries outside the window. Count remaining.

```
Window: 1000ms, Limit: 3

Time 100ms:  log=[100]           → count=1 ≤ 3 → ALLOWED
Time 300ms:  log=[100,300]       → count=2 ≤ 3 → ALLOWED
Time 700ms:  log=[100,300,700]   → count=3 ≤ 3 → ALLOWED
Time 900ms:  log=[100,300,700]   → count=3 = 3  → REJECTED!
Time 1200ms: log=[300,700,1200]  → removed 100 (outside window) → count=3 → ALLOWED
```

**Pros:** Most accurate, no boundary issues
**Cons:** Memory grows with request count (stores all timestamps)

```java
class SlidingWindowLogRateLimiter {
    int maxRequests;
    long windowSizeMs;
    Map<String, LinkedList<Long>> requestLogs;
    
    boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        LinkedList<Long> log = requestLogs.get(clientId);
        
        // Prune old entries
        while (!log.isEmpty() && now - log.peekFirst() > windowSizeMs)
            log.pollFirst();
        
        if (log.size() < maxRequests) {
            log.addLast(now);
            return true;
        }
        return false;
    }
}
```

### Algorithm 4: Leaky Bucket

**Analogy:** Requests enter a bucket (queue). Requests are processed at a CONSTANT rate. If the bucket is full, new requests are rejected.

```
Bucket capacity=3, processing rate=1/sec

Request 1: bucket=[●○○] → accepted
Request 2: bucket=[●●○] → accepted
Request 3: bucket=[●●●] → accepted
Request 4: bucket is FULL → REJECTED!

... 1 second passes, one "leaks" out ...

bucket=[●●○] → can accept 1 more
```

**Pros:** Smooths out bursts, constant output rate
**Cons:** Doesn't allow ANY bursts (even legitimate ones)

**Key difference from Token Bucket:**
- Token Bucket: controls **input rate** (allows bursts up to capacity)
- Leaky Bucket: controls **output rate** (constant, smooth processing)

```java
class LeakyBucketRateLimiter {
    int bucketSize;
    Map<String, LinkedBlockingQueue<Long>> buckets;
    
    boolean allowRequest(String clientId) {
        LinkedBlockingQueue<Long> bucket = buckets.get(clientId);
        return bucket.offer(System.currentTimeMillis()); // false if full
    }
    
    void leak(String clientId) {
        buckets.get(clientId).poll(); // process one request
    }
}
```

---

## Part 3: Algorithm Comparison

| Algorithm | Accuracy | Memory | Allows Bursts | Complexity |
|---|---|---|---|---|
| **Token Bucket** | Good | O(1) per client | ✅ Yes | O(1) |
| **Fixed Window** | Has boundary issue | O(1) per client | ❌ At boundaries | O(1) |
| **Sliding Window Log** | Best | O(n) per client | ❌ No | O(n) cleanup |
| **Leaky Bucket** | Good | O(bucket size) | ❌ No | O(1) |

### Which to Choose?
- **API rate limiting:** Token Bucket (allows bursts, simple)
- **Precise limiting:** Sliding Window Log (most accurate)
- **Traffic shaping:** Leaky Bucket (smooth output)
- **Simple counter:** Fixed Window (if boundary issue is acceptable)

---

## Part 4: Design — Strategy Pattern

All algorithms implement the same interface:

```java
public interface RateLimiter {
    boolean allowRequest(String clientId);
    String getAlgorithmName();
}

class TokenBucketRateLimiter implements RateLimiter { ... }
class FixedWindowRateLimiter implements RateLimiter { ... }
class SlidingWindowLogRateLimiter implements RateLimiter { ... }
class LeakyBucketRateLimiter implements RateLimiter { ... }
```

**Why interface?** Swap algorithms without changing the calling code. Classic Strategy Pattern.

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Distributed rate limiting? | Use Redis with atomic Lua scripts. Each instance checks the shared counter. |
| How does API Gateway do it? | Typically Token Bucket or Sliding Window. Kong, NGINX use these. |
| What headers to return? | `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`, HTTP 429. |
| Thread safety? | Use `synchronized`, `ReentrantLock`, or `ConcurrentHashMap` + atomic ops. |
| Per-user vs per-IP vs global? | Use different `clientId` keys: userId, IP, or a global constant. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Strategy** | RateLimiter interface — swappable algorithms |
| **SRP** | Each algorithm in its own class |
| **OCP** | Add new algorithms without changing existing code |
| **Thread Safety** | `synchronized` methods, `ConcurrentHashMap` |

---

📁 **Source code:** `src/ratelimiter/`

