# Problem 17: Pub-Sub Message Queue

| Pattern | Why |
|---|---|
| **Observer** | Subscribers notified when messages are published |
| **Producer-Consumer** | Producers publish, consumers consume at their own pace |

## Key Design — Kafka-like

### Append-Only Log
```java
class PubSubTopic {
    List<Message> messages;  // never deleted — append only
    
    void publish(Message msg) {
        messages.add(msg);       // append
        notifySubscribers(msg);  // push to live subscribers
    }
}
```

### Consumer Offsets
Each consumer tracks its own **offset** (position in the log):
```
Messages: [msg0] [msg1] [msg2] [msg3] [msg4]
                          ↑              ↑
                    Consumer A      Consumer B
                    (offset=2)      (offset=4)
```
- Consumers can **replay** from any offset
- Different consumers read at different speeds
- Messages are NEVER deleted (unlike traditional queues)

### Entities
```
Message    → key, value, timestamp
Producer   → publishes to a topic
Consumer   → reads from a topic at its own offset
PubSubTopic → the append-only log of messages
Subscriber  → gets real-time push notifications
```

## vs Notification System
| Feature | Notification System | Pub-Sub Queue |
|---|---|---|
| Delivery | Push to all subscribers | Pull by consumers at own pace |
| Persistence | Fire-and-forget | Messages stored, replayable |
| Use case | Alerts, emails | Event streaming, log processing |

📁 `src/pubsub/`

