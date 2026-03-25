package pubsub;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumer with offset tracking per topic.
 * Can replay messages from a specific offset.
 */
public class Consumer implements Subscriber {
    private final String name;
    private final Map<String, Integer> offsets; // topic -> current offset

    public Consumer(String name) {
        this.name = name;
        this.offsets = new HashMap<>();
    }

    @Override
    public void onMessage(String topic, Message message) {
        System.out.println("    -> " + name + " received from [" + topic + "]: " + message);
        offsets.merge(topic, 1, Integer::sum);
    }

    /**
     * Pull messages from a topic starting at current offset (polling mode).
     */
    public void poll(PubSubTopic topic, int maxMessages) {
        int offset = offsets.getOrDefault(topic.getName(), 0);
        int count = 0;
        System.out.println("  " + name + " polling [" + topic.getName() + "] from offset " + offset + ":");
        while (count < maxMessages) {
            Message msg = topic.getMessageAtOffset(offset);
            if (msg == null) break;
            System.out.println("    " + name + " polled: " + msg);
            offset++;
            count++;
        }
        offsets.put(topic.getName(), offset);
        if (count == 0) System.out.println("    No new messages.");
    }

    /**
     * Reset offset to replay messages.
     */
    public void resetOffset(String topicName, int offset) {
        offsets.put(topicName, offset);
        System.out.println("  " + name + " reset offset for [" + topicName + "] to " + offset);
    }

    @Override
    public String getName() { return name; }
}

