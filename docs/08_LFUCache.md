# Problem 4: LFU Cache — Complete Guide

---

## Part 1: Understanding the Problem

### What is an LFU Cache?
- **LFU = Least Frequently Used**
- A cache with a fixed capacity
- When full, it evicts the item that has been accessed the **fewest times**
- If two items have the **same frequency**, evict the **least recently used** one (LRU tiebreaker)
- Both `get()` and `put()` must be **O(1)**

### LRU vs LFU — What's the Difference?

| Aspect | LRU | LFU |
|---|---|---|
| Eviction criteria | Least **recently** used | Least **frequently** used |
| Tracks | **Order** of access | **Count** of accesses |
| Evicts | Item not touched for longest time | Item touched fewest times total |
| Use case | "What haven't I used lately?" | "What do I barely ever use?" |

### Example
```
Cache capacity = 2
put(1, "A")    → freq(1)=1
put(2, "B")    → freq(2)=1
get(1)         → freq(1)=2    ← accessed again
get(1)         → freq(1)=3    ← accessed again
put(3, "C")    → Full! Who to evict?
  LRU would evict key 2 (least recently used)  ← SAME ANSWER
  LFU evicts key 2 (freq=1, lowest frequency)  ← SAME ANSWER

But consider:
get(2) get(2) get(2) get(2)  → freq(2)=5
put(3, "C") → Full! Who to evict?
  LRU evicts key 1 (used longer ago)
  LFU evicts key 1 (freq=3, lower than key 2's freq=5)  ← DIFFERENT!
```

---

## Part 2: The Key Insight — Three Maps + minFreq

The trick to O(1) everything is using **3 HashMaps** + a **minFreq tracker**:

```
keyToVal:    { 1→"A", 2→"B", 3→"C" }         ← stores actual values
keyToFreq:   { 1→3,   2→1,   3→1   }         ← how many times each key was accessed
freqToKeys:  { 1→{2,3}, 3→{1} }              ← groups keys by frequency
                  ↑                              (LinkedHashSet for LRU order!)
                  └── minFreq = 1
```

### Why Each Map?

| Map | Provides | Why needed? |
|---|---|---|
| `keyToVal` | O(1) value lookup | Core cache functionality |
| `keyToFreq` | O(1) frequency lookup per key | Know which freq bucket a key is in |
| `freqToKeys` | O(1) access to keys at any frequency | Find who to evict (minFreq bucket) |
| `minFreq` (int) | O(1) find lowest frequency | Jump straight to the eviction bucket |

### Why `LinkedHashSet` (not just `HashSet`)?

`LinkedHashSet` maintains **insertion order**! This gives us the **LRU tiebreaker**:
- Keys in the same frequency bucket are ordered by when they entered that bucket
- The **first** element (iterator().next()) is the **least recently used** among them

---

## Part 3: The Operations — Explained

### `get(key)`
```java
public V get(K key) {
    if (!keyToVal.containsKey(key)) return null;  // cache miss
    increaseFreq(key);                             // bump frequency
    return keyToVal.get(key);
}
```

### `put(key, value)`
```java
public void put(K key, V value) {
    if (keyToVal.containsKey(key)) {
        keyToVal.put(key, value);    // update value
        increaseFreq(key);           // bump frequency
        return;
    }
    
    if (keyToVal.size() >= capacity) {
        evictLFU();                  // make room
    }
    
    // Insert new key with freq = 1
    keyToVal.put(key, value);
    keyToFreq.put(key, 1);
    freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    minFreq = 1;                     // new key always has freq=1!
}
```

**Why `minFreq = 1` on insert?**
A brand new key has been accessed exactly once (right now). Since 1 is the smallest possible frequency, it's always the new minimum.

### `increaseFreq(key)` — The Core Move
```java
private void increaseFreq(K key) {
    int freq = keyToFreq.get(key);
    keyToFreq.put(key, freq + 1);           // update freq map
    
    // Remove from current freq bucket
    LinkedHashSet<K> keys = freqToKeys.get(freq);
    keys.remove(key);
    if (keys.isEmpty()) {
        freqToKeys.remove(freq);
        if (minFreq == freq) minFreq++;      // CRITICAL!
    }
    
    // Add to freq+1 bucket
    freqToKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
}
```

**The critical `minFreq++`:**
When a key moves from frequency `f` to `f+1`, and the frequency `f` bucket becomes **empty**, we need to bump `minFreq`. Otherwise we'd try to evict from an empty bucket!

### `evictLFU()` — Evict the Least Frequent
```java
private void evictLFU() {
    LinkedHashSet<K> keys = freqToKeys.get(minFreq);
    K evictKey = keys.iterator().next();     // first = LRU among this freq
    keys.remove(evictKey);
    if (keys.isEmpty()) {
        freqToKeys.remove(minFreq);
    }
    keyToVal.remove(evictKey);
    keyToFreq.remove(evictKey);
}
```

**Why `iterator().next()`?**
`LinkedHashSet` maintains insertion order. The **first** item was inserted into this frequency bucket **earliest** → it's the LRU tiebreaker.

---

## Part 4: Walkthrough Example

```
Cache capacity = 3

put(1, "A"):
  keyToVal: {1→"A"}, keyToFreq: {1→1}, freqToKeys: {1→{1}}
  minFreq = 1

put(2, "B"):
  keyToVal: {1→"A", 2→"B"}, keyToFreq: {1→1, 2→1}, freqToKeys: {1→{1,2}}
  minFreq = 1

put(3, "C"):
  keyToVal: {1→"A", 2→"B", 3→"C"}, keyToFreq: {1→1, 2→1, 3→1}
  freqToKeys: {1→{1,2,3}}
  minFreq = 1

get(1):  → freq 1→2, move key 1 from bucket 1 to bucket 2
  keyToFreq: {1→2, 2→1, 3→1}
  freqToKeys: {1→{2,3}, 2→{1}}
  minFreq = 1  (bucket 1 still has keys)

get(2):  → freq 1→2, move key 2 from bucket 1 to bucket 2
  keyToFreq: {1→2, 2→2, 3→1}
  freqToKeys: {1→{3}, 2→{1,2}}
  minFreq = 1

get(1):  → freq 2→3, move key 1 from bucket 2 to bucket 3
  keyToFreq: {1→3, 2→2, 3→1}
  freqToKeys: {1→{3}, 2→{2}, 3→{1}}
  minFreq = 1

put(4, "D"):  → FULL! Evict from minFreq=1 bucket → key 3
  Remove key 3 from everywhere
  Insert key 4 with freq=1
  keyToVal: {1→"A", 2→"B", 4→"D"}
  keyToFreq: {1→3, 2→2, 4→1}
  freqToKeys: {1→{4}, 2→{2}, 3→{1}}
  minFreq = 1
```

---

## Part 5: Complexity Analysis

| Operation | Time | Space |
|---|---|---|
| `get()` | O(1) | — |
| `put()` | O(1) | — |
| Overall space | — | O(capacity) |

**Why O(1)?**
- All HashMap operations: O(1)
- LinkedHashSet add/remove/iterator.next: O(1)
- `minFreq` tracker: O(1) access

---

## Part 6: LRU vs LFU — When to Use Which?

| Scenario | Best Choice | Why |
|---|---|---|
| Web page caching | LRU | Recent pages likely needed again soon |
| CDN caching | LFU | Popular content should stay cached |
| Database query cache | LRU | Recent queries likely repeated |
| API rate limiting | LFU | Frequent callers are the ones to track |
| In-memory config cache | LFU | Frequently accessed configs should stay |

---

## Part 7: Common Interview Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using only a HashMap + counter | No LRU tiebreaker — need LinkedHashSet order |
| Forgetting `minFreq = 1` on new insert | Will try to evict from wrong bucket |
| Not handling empty freq buckets | `minFreq` becomes stale |
| Using `TreeMap` for frequencies | O(log n) instead of O(1) |

---

## Part 8: Follow-Up Questions

| Question | Answer |
|---|---|
| Thread safety? | Wrap get/put in `synchronized` or use `ReentrantLock`. |
| What if we want decay (reduce old frequencies)? | Add a decay factor: periodically halve all frequencies. Prevents "cache pollution" from old items. |
| Real-world use? | Redis uses a probabilistic LFU approximation. |
| Can you combine LRU and LFU? | Yes! That's called **ARC** (Adaptive Replacement Cache) — used in ZFS filesystem. |

---

📁 **Source code:** `src/lfucache/`

