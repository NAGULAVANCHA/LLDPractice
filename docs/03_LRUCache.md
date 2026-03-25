# Problem 3: LRU Cache — Complete Guide

---

## Part 1: Understanding the Problem

### What is an LRU Cache?
- **LRU = Least Recently Used**
- A cache with a **fixed capacity**
- When the cache is full and you add a new item, it **evicts the least recently used** item
- Both `get()` and `put()` must be **O(1)** — that's the interview constraint!

### Real World Example
Your browser keeps the last N web pages cached. When memory is full, it throws away the page you haven't visited in the longest time.

### Requirements
- `get(key)` → return value if exists, else null. **Marks as recently used.**
- `put(key, value)` → insert/update. If full, evict LRU item. **Marks as recently used.**
- Both operations: **O(1) time**

---

## Part 2: The Key Insight — Why TWO Data Structures?

### Can we use just a HashMap?
```
HashMap: { A:1, B:2, C:3 }
```
- ✅ O(1) lookup
- ❌ **No order!** We can't tell which key was used most/least recently

### Can we use just a Linked List?
```
LinkedList: [C] → [A] → [B]  (most recent → least recent)
```
- ✅ Maintains order
- ❌ **O(n) lookup!** To find key "B", we scan the entire list

### Solution: Use BOTH Together!

```
HashMap:  { A → nodeA, B → nodeB, C → nodeC }
                ↓          ↓          ↓
DLL:     [HEAD] ↔ [C] ↔ [A] ↔ [B] ↔ [TAIL]
          dummy   MRU              LRU   dummy
```

| Data Structure | Provides |
|---|---|
| **HashMap<Key, Node>** | O(1) lookup by key |
| **Doubly Linked List** | O(1) add/remove + maintains usage order |

Together → **O(1) for everything!**

---

## Part 3: The Data Structure

### Doubly Linked List Node
```java
private class Node {
    K key;
    V value;
    Node prev, next;

    Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```

**Why store `key` in the node?**
When we evict the LRU node (tail.prev), we need its key to also remove it from the HashMap!

### Sentinel (Dummy) Nodes
```java
head = new Node(null, null);   // dummy head
tail = new Node(null, null);   // dummy tail
head.next = tail;
tail.prev = head;
```

**Why dummy nodes?**
- Without them, you'd need null checks everywhere:
  ```java
  // WITHOUT sentinels — messy!
  if (head == null) { head = newNode; }
  else { head.prev = newNode; newNode.next = head; head = newNode; }
  ```
- With sentinels, `addToFront` and `removeNode` are always the same — no edge cases!

### Visual After Init
```
[HEAD] ↔ [TAIL]
dummy     dummy

Real nodes go BETWEEN head and tail.
```

---

## Part 4: The Operations — Explained

### `get(key)`
```java
public V get(K key) {
    Node node = map.get(key);           // O(1) HashMap lookup
    if (node == null) return null;       // cache miss
    
    removeNode(node);                    // remove from current position
    addToFront(node);                    // move to front (most recent)
    return node.value;
}
```

**Why move to front on GET?**
Because accessing a key means the user just used it → it's now the MOST recently used.

### `put(key, value)`
```java
public void put(K key, V value) {
    Node existing = map.get(key);
    if (existing != null) {
        // UPDATE existing
        existing.value = value;
        removeNode(existing);
        addToFront(existing);            // mark as most recent
    } else {
        // INSERT new
        if (map.size() >= capacity) {
            // EVICT: remove the LRU (node just before tail)
            Node lru = tail.prev;
            removeNode(lru);
            map.remove(lru.key);         // ← this is why Node stores key!
        }
        Node newNode = new Node(key, value);
        addToFront(newNode);
        map.put(key, newNode);
    }
}
```

### Internal List Operations
```java
private void addToFront(Node node) {
    node.next = head.next;       // new node points to old first
    node.prev = head;            // new node points back to head
    head.next.prev = node;       // old first points back to new node
    head.next = node;            // head points to new node
}

private void removeNode(Node node) {
    node.prev.next = node.next;  // skip over this node (forward)
    node.next.prev = node.prev;  // skip over this node (backward)
}
```

**Visual — addToFront(X):**
```
Before: [HEAD] ↔ [A] ↔ [B] ↔ [TAIL]
After:  [HEAD] ↔ [X] ↔ [A] ↔ [B] ↔ [TAIL]
```

**Visual — removeNode(A):**
```
Before: [HEAD] ↔ [X] ↔ [A] ↔ [B] ↔ [TAIL]
After:  [HEAD] ↔ [X] ↔ [B] ↔ [TAIL]
```

---

## Part 5: Walkthrough Example

```
Cache capacity = 3

put(1, "A"):  [HEAD] ↔ [1=A] ↔ [TAIL]
put(2, "B"):  [HEAD] ↔ [2=B] ↔ [1=A] ↔ [TAIL]
put(3, "C"):  [HEAD] ↔ [3=C] ↔ [2=B] ↔ [1=A] ↔ [TAIL]

get(1):       Move 1 to front
              [HEAD] ↔ [1=A] ↔ [3=C] ↔ [2=B] ↔ [TAIL]

put(4, "D"):  Cache is FULL! Evict LRU = tail.prev = [2=B]
              Remove 2 from list and map
              Add 4 to front
              [HEAD] ↔ [4=D] ↔ [1=A] ↔ [3=C] ↔ [TAIL]

get(2):       Returns null (was evicted)
```

---

## Part 6: Complexity Analysis

| Operation | Time | Space |
|---|---|---|
| `get()` | O(1) | — |
| `put()` | O(1) | — |
| Overall space | — | O(capacity) |

**Why O(1)?**
- HashMap get/put: O(1)
- addToFront: O(1) — just pointer updates
- removeNode: O(1) — just pointer updates
- No iteration anywhere!

---

## Part 7: Common Interview Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using a singly linked list | Can't do O(1) removal without `prev` pointer |
| Forgetting to store key in Node | Can't remove from HashMap during eviction |
| Not using sentinel nodes | Leads to null-check bugs |
| Using `LinkedHashMap` directly | Interviewer wants you to build it from scratch! |
| Forgetting to move to front on `get()` | `get()` also counts as "using" the item |

---

## Part 8: Follow-Up Questions

| Question | Answer |
|---|---|
| Thread safety? | Add `synchronized` to get/put, or use `ReentrantReadWriteLock` for concurrent reads. |
| What's the Java built-in? | `LinkedHashMap` with `accessOrder=true` and override `removeEldestEntry()`. Show you know this, then build from scratch. |
| TTL (time-to-live) per entry? | Store `expiryTime` in Node. On `get()`, check if expired → treat as miss. Lazy cleanup. |
| How about LFU instead? | Track frequency per key. See the LFU Cache problem! |

---

## Part 9: Patterns Recap

| Pattern / Concept | Where & Why |
|---|---|
| **HashMap + Doubly Linked List** | Core data structure for O(1) everything |
| **Sentinel Nodes** | Eliminate null-check edge cases |
| **Generics `<K, V>`** | Cache works with any key/value types |
| **Encapsulation** | Node is private inner class — implementation detail |
| **Immutability** | capacity is `final` — can't change after creation |

---

📁 **Source code:** `src/lrucache/`

