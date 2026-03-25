package lfucache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * LFU CACHE (Least Frequently Used)
 * ===================================
 * Key Concepts:
 *  - Evicts the LEAST FREQUENTLY accessed item
 *  - If tie in frequency, evict the LEAST RECENTLY used among them (LRU tiebreaker)
 *
 * Data Structures (all O(1)):
 *  1. keyToVal:   HashMap<Key, Value>          — stores the actual values
 *  2. keyToFreq:  HashMap<Key, Integer>        — tracks frequency of each key
 *  3. freqToKeys: HashMap<Freq, LinkedHashSet<Key>> — groups keys by frequency
 *                 LinkedHashSet maintains insertion order (LRU tiebreaker!)
 *  4. minFreq:    int — tracks the current minimum frequency for O(1) eviction
 *
 * Time Complexity: O(1) for get() and put()
 * Space Complexity: O(capacity)
 *
 * Interview Tip:
 *  - The trick is the 3-map approach + minFreq tracker
 *  - LinkedHashSet gives LRU ordering within same frequency
 *  - When we access a key, we move it from freq bucket to freq+1 bucket
 *  - When the minFreq bucket becomes empty, increment minFreq
 */
public class LFUCache<K, V> {

    private final int capacity;
    private int minFreq;

    private final Map<K, V> keyToVal;
    private final Map<K, Integer> keyToFreq;
    private final Map<Integer, LinkedHashSet<K>> freqToKeys;

    public LFUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyToVal = new HashMap<>();
        this.keyToFreq = new HashMap<>();
        this.freqToKeys = new HashMap<>();
    }

    public V get(K key) {
        if (!keyToVal.containsKey(key)) return null;
        increaseFreq(key);
        return keyToVal.get(key);
    }

    public void put(K key, V value) {
        if (keyToVal.containsKey(key)) {
            keyToVal.put(key, value);
            increaseFreq(key);
            return;
        }

        // Need to evict if at capacity
        if (keyToVal.size() >= capacity) {
            evictLFU();
        }

        // Insert new key with freq=1
        keyToVal.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1; // new key always has freq=1
    }

    /**
     * Move key from freq bucket to freq+1 bucket.
     */
    private void increaseFreq(K key) {
        int freq = keyToFreq.get(key);
        keyToFreq.put(key, freq + 1);

        // Remove from current freq bucket
        LinkedHashSet<K> keys = freqToKeys.get(freq);
        keys.remove(key);
        if (keys.isEmpty()) {
            freqToKeys.remove(freq);
            if (minFreq == freq) minFreq++; // important!
        }

        // Add to freq+1 bucket
        freqToKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    /**
     * Evict the least frequently used key.
     * Among ties, evict the least recently used (first in LinkedHashSet).
     */
    private void evictLFU() {
        LinkedHashSet<K> keys = freqToKeys.get(minFreq);
        K evictKey = keys.iterator().next(); // first = LRU among this freq
        keys.remove(evictKey);
        if (keys.isEmpty()) {
            freqToKeys.remove(minFreq);
        }
        keyToVal.remove(evictKey);
        keyToFreq.remove(evictKey);
        System.out.println("  [Evicted: " + evictKey + " (freq=" + minFreq + ")]");
    }

    public int size() { return keyToVal.size(); }

    public void display() {
        System.out.println("  Cache contents: " + keyToVal);
        System.out.println("  Frequencies:    " + keyToFreq);
        System.out.println("  Min frequency:  " + minFreq);
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== LFU Cache Demo (capacity=3) ===\n");
        LFUCache<Integer, String> cache = new LFUCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        cache.display();

        // Access key 1 and 2 (increase their freq)
        System.out.println("\nget(1) = " + cache.get(1)); // freq: 1->2
        System.out.println("get(2) = " + cache.get(2)); // freq: 1->2
        System.out.println("get(1) = " + cache.get(1)); // freq: 2->3
        cache.display();
        // Now: key1 freq=3, key2 freq=2, key3 freq=1
        // minFreq = 1

        System.out.println("\nput(4, D) — should evict key 3 (lowest freq=1)");
        cache.put(4, "D");
        cache.display();

        System.out.println("\nget(3) = " + cache.get(3)); // null — evicted

        // Now key4 has freq=1, key2 has freq=2, key1 has freq=3
        System.out.println("\nput(5, E) — should evict key 4 (lowest freq=1)");
        cache.put(5, "E");
        cache.display();
    }
}

