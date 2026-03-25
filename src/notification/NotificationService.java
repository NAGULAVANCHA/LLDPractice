package notification;

import java.util.HashMap;
import java.util.Map;

public class NotificationService {
    private final Map<String, Topic> topics;

    public NotificationService() {
        this.topics = new HashMap<>();
    }

    public Topic createTopic(String name) {
        Topic topic = new Topic(name);
        topics.put(name, topic);
        return topic;
    }

    public Topic getTopic(String name) {
        return topics.get(name);
    }

    public void subscribe(String topicName, User user) {
        Topic topic = topics.get(topicName);
        if (topic != null) topic.subscribe(user);
        else System.out.println("Topic not found: " + topicName);
    }

    public void publish(String topicName, String message) {
        Topic topic = topics.get(topicName);
        if (topic != null) topic.publish(message);
        else System.out.println("Topic not found: " + topicName);
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== Notification System Demo ===\n");

        NotificationService service = new NotificationService();

        // Create topics
        service.createTopic("order-updates");
        service.createTopic("promotions");

        // Create users with preferred channels
        User alice = new User("Alice", "alice@mail.com", "+1-111");
        alice.addChannel(new EmailChannel());
        alice.addChannel(new PushNotificationChannel());

        User bob = new User("Bob", "bob@mail.com", "+1-222");
        bob.addChannel(new SMSChannel());

        User charlie = new User("Charlie", "charlie@mail.com", "+1-333");
        charlie.addChannel(new EmailChannel());
        charlie.addChannel(new SMSChannel());
        charlie.addChannel(new PushNotificationChannel());

        // Subscribe
        service.subscribe("order-updates", alice);
        service.subscribe("order-updates", bob);
        service.subscribe("promotions", alice);
        service.subscribe("promotions", charlie);

        // Publish events
        service.publish("order-updates", "Your order #1234 has been shipped!");
        service.publish("promotions", "50% OFF on all items this weekend!");
    }
}

