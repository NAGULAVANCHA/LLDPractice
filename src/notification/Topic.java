package notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Topic (Subject in Observer pattern).
 * Users subscribe to topics and get notified on events.
 */
public class Topic {
    private final String name;
    private final List<User> subscribers;

    public Topic(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println(user.getName() + " subscribed to [" + name + "]");
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println(user.getName() + " unsubscribed from [" + name + "]");
    }

    public void publish(String message) {
        System.out.println("\n📢 Publishing to [" + name + "]: " + message);
        for (User user : subscribers) {
            user.notify(message);
        }
    }

    public String getName() { return name; }
}

