package lrucache;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU CACHE (Least Recently Used)
 * ================================
 * Key Concepts:
 *  - HashMap + Doubly Linked List for O(1) get & put
 *  - When cache is full, EVICT the least recently used item
 *    (the item at the TAIL of the linked list)
 *  - Every access (get/put) moves the item to the HEAD (most recent)
 *
 * Data Structures:
 *  HashMap<Key, Node>  -> O(1) lookup
 *  Doubly Linked List  -> O(1) add/remove (maintains usage order)
 *
 * Time Complexity: O(1) for both get() and put()
 * Space Complexity: O(capacity)
 *
 * Interview Tip:
 *  - Always explain WHY you need both structures
 *  - HashMap alone can't track order
 *  - LinkedList alone can't do O(1) lookup
 *  - Together they give O(1) for everything
 */
public class LRUCache<K, V> {

    // --- Doubly Linked List Node ---
    private class Node {
        K key;
        V value;
        Node prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node> map;
    // Sentinel (dummy) head and tail — avoids null checks
    private final Node head;
    private final Node tail;

    public LRUCache(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
        this.map = new HashMap<>();

        // Initialize dummy head <-> tail
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * GET: If key exists, move to head (most recent) and return value.
     */
    public V get(K key) {
        Node node = map.get(key);
        if (node == null) return null;

        // Move to front (most recently used)
        removeNode(node);
        addToFront(node);
        return node.value;
    }

    /**
     * PUT: Insert or update. If full, evict LRU (tail.prev).
     */
    public void put(K key, V value) {
        Node existing = map.get(key);
        if (existing != null) {
            existing.value = value;
            removeNode(existing);
            addToFront(existing);
        } else {
            if (map.size() >= capacity) {
                // Evict LRU: the node just before tail
                Node lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
                System.out.println("  [Evicted: " + lru.key + "]");
            }
            Node newNode = new Node(key, value);
            addToFront(newNode);
            map.put(key, newNode);
        }
    }

    // --- Internal linked list operations ---

    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public int size() { return map.size(); }

    public void display() {
        System.out.print("  Cache [MRU -> LRU]: ");
        Node curr = head.next;
        while (curr != tail) {
            System.out.print(curr.key + "=" + curr.value);
            if (curr.next != tail) System.out.print(" -> ");
            curr = curr.next;
        }
        System.out.println();
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== LRU Cache Demo (capacity=3) ===\n");
        LRUCache<Integer, String> cache = new LRUCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        cache.display(); // 3=C -> 2=B -> 1=A

        System.out.println("\nget(1) = " + cache.get(1)); // moves 1 to front
        cache.display(); // 1=A -> 3=C -> 2=B

        System.out.println("\nput(4, D) — should evict key 2 (LRU)");
        cache.put(4, "D");
        cache.display(); // 4=D -> 1=A -> 3=C

        System.out.println("\nget(2) = " + cache.get(2)); // null — evicted

        System.out.println("\nput(5, E) — should evict key 3");
        cache.put(5, "E");
        cache.display(); // 5=E -> 4=D -> 1=A
    }
}

