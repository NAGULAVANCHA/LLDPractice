package pubsub;

/**
 * PUB-SUB MESSAGE QUEUE LLD (Kafka-like)
 * ========================================
 * Key Concepts:
 *  - Observer Pattern:    Subscribers receive messages published to topics
 *  - Producer/Consumer:   Decoupled message producers and consumers
 *  - Queue per topic:     Each topic has an ordered log of messages
 *  - Consumer offset:     Each consumer tracks its own read position
 *  - Thread safety:       Synchronized operations
 *
 * Interview Points:
 *  - Topic partitioning (simplified here as single partition)
 *  - Consumer groups: Multiple consumers in a group share the load
 *  - Offset management: Consumers can replay messages
 *  - At-least-once vs exactly-once delivery semantics
 */
public class Message {
    private static int counter = 0;
    private final int id;
    private final String content;
    private final long timestamp;

    public Message(String content) {
        this.id = ++counter;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() { return "Msg#" + id + ": " + content; }
}

