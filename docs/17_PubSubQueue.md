# Problem 17: Pub-Sub Message Queue (Kafka-like) — Complete Guide

---

## Part 1: Understanding the Problem

A simplified Kafka-like message queue where producers publish messages to topics, and consumers read them at their own pace.

### Requirements
- ✓ Topics hold an **append-only log** of messages (never deleted)
- ✓ Producers publish to topics
- ✓ Consumers track their own **offset** (read position)
- ✓ Consumers can **replay** from any offset
- ✓ Push mode (real-time) AND pull mode (polling)

---

## Part 2: The Key Insight — Offset-Based Consumption

### Traditional Queue vs Kafka-like Log

| Feature | Traditional Queue | Kafka-like Log (Our Design) |
|---|---|---|
| After reading | Message is REMOVED | Message STAYS in log |
| Multiple readers | Only one gets the message | ALL can read independently |
| Replay | ❌ Can't re-read | ✅ Reset offset, replay from start |
| Consumer speed | Must keep up or lose messages | Each consumer reads at own pace |

### How Offsets Work
```
Topic "orders":
  Messages: [msg0] [msg1] [msg2] [msg3] [msg4]
                                   ↑              ↑
                           Consumer A         Consumer B
                           (offset=3)         (offset=5)
```
- Consumer A has read 3 messages, next read starts at offset 3
- Consumer B has read all 5, next read returns nothing
- New consumer starts at offset 0 — reads ALL historical messages

---

## Part 3: The Code — Explained

### Message — Immutable Event
```java
public class Message {
    private final int id;
    private final String content;
    private final long timestamp;
}
```

### Topic — Append-Only Log
```java
public class PubSubTopic {
    private final String name;
    private final List<Message> messages;         // append-only log
    private final List<Subscriber> subscribers;   // for push mode

    public synchronized void publish(Message message) {
        messages.add(message);                     // append (never delete)
        notifySubscribers(message);                // push to live listeners
    }

    public synchronized Message getMessageAtOffset(int offset) {
        if (offset < 0 || offset >= messages.size()) return null;
        return messages.get(offset);               // O(1) random access
    }
}
```

### Consumer — Two Modes
```java
public class Consumer implements Subscriber {
    private final Map<String, Integer> offsets;  // topic → current offset

    // PUSH MODE: receive real-time notifications
    @Override
    public void onMessage(String topic, Message message) {
        System.out.println(name + " received: " + message);
        offsets.merge(topic, 1, Integer::sum);     // advance offset
    }

    // PULL MODE: poll for messages at own pace
    public void poll(PubSubTopic topic, int maxMessages) {
        int offset = offsets.getOrDefault(topic.getName(), 0);
        int count = 0;
        while (count < maxMessages) {
            Message msg = topic.getMessageAtOffset(offset);
            if (msg == null) break;                // no more messages
            System.out.println(name + " polled: " + msg);
            offset++;
            count++;
        }
        offsets.put(topic.getName(), offset);       // save position
    }

    // REPLAY: reset offset to re-read messages
    public void resetOffset(String topic, int newOffset) {
        offsets.put(topic, newOffset);
    }
}
```

### Producer — Simple Publisher
```java
public class Producer {
    public void send(PubSubTopic topic, String content) {
        topic.publish(new Message(content));
    }
}
```

---

## Part 4: Data Flow

```
1. OrderService publishes "Order #1001 placed" to [orders]
   → messages: [msg1]
   → Push: OrderProcessor receives, Logger receives

2. OrderService publishes "Order #1002 placed"
   → messages: [msg1, msg2]

3. Latecomer consumer joins and polls:
   → poll(orders, 10)
   → offset=0: read msg1
   → offset=1: read msg2
   → offset=2: null → stop
   → Latecomer has now read ALL historical messages!

4. Latecomer.resetOffset("orders", 0)
   → poll(orders, 1)
   → Re-reads msg1 from the beginning (replay!)
```

---

## Part 5: Pub-Sub vs Notification System

| Feature | Notification System (Problem 5) | Pub-Sub Queue (This) |
|---|---|---|
| Pattern | Observer (push only) | Producer-Consumer (push + pull) |
| Messages | Fire-and-forget | Stored, replayable |
| Late subscribers | Miss past messages | Can read ALL history |
| Use case | Alerts, emails, SMS | Event streaming, logging, analytics |

---

## Part 6: Follow-Up Questions

| Question | Answer |
|---|---|
| Consumer groups? | Multiple consumers share a group. Each message goes to ONE consumer in the group (load balancing). |
| Partitioning? | Split a topic into N partitions. Each consumer in a group reads from assigned partitions. |
| Delivery guarantees? | At-most-once (no retry), at-least-once (retry, may duplicate), exactly-once (hardest, use idempotency keys). |
| Message retention? | Add TTL. Background thread cleans messages older than N hours. Adjust offsets accordingly. |

---

## Part 7: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Observer** | Subscribers get real-time push notifications |
| **Producer-Consumer** | Decoupled publishing and consumption |
| **Offset Tracking** | Each consumer reads independently |
| **Append-Only Log** | Messages are never deleted — supports replay |
| **Thread Safety** | `synchronized` on publish and getMessageAtOffset |

---

📁 **Source code:** `src/pubsub/`
