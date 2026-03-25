package pubsub;

import java.util.ArrayList;
import java.util.List;

/**
 * A topic is an ordered log of messages.
 * Messages are never deleted (append-only, like Kafka).
 */
public class PubSubTopic {
    private final String name;
    private final List<Message> messages;
    private final List<Subscriber> subscribers;

    public PubSubTopic(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public synchronized void publish(Message message) {
        messages.add(message);
        System.out.println("  Published to [" + name + "]: " + message);
        notifySubscribers(message);
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
        System.out.println("  " + subscriber.getName() + " subscribed to [" + name + "]");
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    private void notifySubscribers(Message message) {
        for (Subscriber sub : subscribers) {
            sub.onMessage(name, message);
        }
    }

    /**
     * Get message at a specific offset (for replay).
     */
    public synchronized Message getMessageAtOffset(int offset) {
        if (offset < 0 || offset >= messages.size()) return null;
        return messages.get(offset);
    }

    public synchronized int size() { return messages.size(); }
    public String getName() { return name; }
}

