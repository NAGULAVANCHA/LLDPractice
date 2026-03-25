# Problem 5: Notification System — Complete Guide

---

## Part 1: Understanding the Problem

### What is a Notification System?
Think of how apps like Swiggy, Amazon, or YouTube notify you:
- You **subscribe** to a topic (e.g., "order-updates", "promotions")
- When an event happens on that topic, you get notified
- You might receive via **Email**, **SMS**, **Push Notification**, or all three

### Requirements
- ✓ Users subscribe to topics
- ✓ Users choose their preferred channels (Email, SMS, Push)
- ✓ When an event is published to a topic, ALL subscribers get notified
- ✓ Each subscriber gets notified via ALL their preferred channels
- ✓ Easy to add new channels without changing existing code

---

## Part 2: The Key Insight — Two Patterns Combined

This problem combines **TWO** design patterns:

### 1. Observer Pattern — "Who gets notified?"
When an event happens, ALL subscribers of that topic get notified automatically.

### 2. Strategy Pattern — "How do they get notified?"
Each user picks their preferred channels (Email, SMS, Push). The notification mechanism is **swappable**.

---

## 🎯 Design Pattern: Observer Pattern

### Problem It Solves
When one object changes, **multiple** other objects need to know about it — without tightly coupling them.

### Real World Analogy
**YouTube Subscriptions:**
- You subscribe to a YouTube channel
- When the creator uploads a new video, ALL subscribers get a notification
- The creator doesn't know/care who the subscribers are — they just "publish"
- Subscribers don't poll the channel — they get **pushed** the update

### How It Works

```
┌────────────────┐     subscribe()     ┌──────────────┐
│                │ ◄───────────────── │   User 1     │
│  Topic         │     subscribe()     ├──────────────┤
│ (Subject)      │ ◄───────────────── │   User 2     │
│                │     subscribe()     ├──────────────┤
│  "order-       │ ◄───────────────── │   User 3     │
│   updates"     │                     └──────────────┘
│                │
│  publish() ────┼──→ notify(User 1)
│                │──→ notify(User 2)
│                │──→ notify(User 3)
└────────────────┘
```

### WITHOUT Observer Pattern ❌
```java
class OrderService {
    void completeOrder(Order order) {
        // order logic...
        emailService.sendEmail(user, "Order shipped!");
        smsService.sendSMS(user, "Order shipped!");
        pushService.sendPush(user, "Order shipped!");
        // What if we add Slack notifications? MODIFY this class!
        // What if we add WhatsApp? MODIFY again!
    }
}
```
- ❌ OrderService knows about EVERY notification channel
- ❌ Adding a channel = modifying OrderService (violates OCP)
- ❌ OrderService has too many responsibilities (violates SRP)

### WITH Observer Pattern ✅
```java
class OrderService {
    void completeOrder(Order order) {
        // order logic...
        topic.publish("Order shipped!");  // ONE LINE!
    }
}
// Users are subscribed to the topic — they get notified automatically
```
- ✅ OrderService doesn't know about channels
- ✅ Adding subscribers = no code changes
- ✅ Clean separation of concerns

---

## Part 3: The Code — Explained

### NotificationChannel (Interface) — Strategy Pattern
```java
public interface NotificationChannel {
    void send(String recipient, String message);
    String getChannelType();
}
```

Each channel type implements this:

```java
public class EmailChannel implements NotificationChannel {
    public void send(String recipient, String message) {
        System.out.println("📧 EMAIL to " + recipient + ": " + message);
    }
    public String getChannelType() { return "EMAIL"; }
}

public class SMSChannel implements NotificationChannel {
    public void send(String recipient, String message) {
        System.out.println("📱 SMS to " + recipient + ": " + message);
    }
    public String getChannelType() { return "SMS"; }
}

public class PushNotificationChannel implements NotificationChannel {
    public void send(String recipient, String message) {
        System.out.println("🔔 PUSH to " + recipient + ": " + message);
    }
    public String getChannelType() { return "PUSH"; }
}
```

**Adding WhatsApp?** Just create `WhatsAppChannel implements NotificationChannel`. ZERO changes to existing code.

### User — The Subscriber
```java
public class User {
    private final String name;
    private final String email;
    private final String phone;
    private final List<NotificationChannel> preferredChannels;

    public void addChannel(NotificationChannel channel) {
        preferredChannels.add(channel);
    }

    public void notify(String message) {
        for (NotificationChannel channel : preferredChannels) {
            String recipient = switch (channel.getChannelType()) {
                case "EMAIL" -> email;
                case "SMS"   -> phone;
                default      -> name;
            };
            channel.send(recipient, message);
        }
    }
}
```

**Key design:** Each user chooses their OWN channels. Alice might want Email + Push. Bob might want only SMS.

### Topic — The Subject (Observer Pattern)
```java
public class Topic {
    private final String name;
    private final List<User> subscribers;

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    public void publish(String message) {
        System.out.println("📢 Publishing to [" + name + "]: " + message);
        for (User user : subscribers) {
            user.notify(message);  // notify ALL subscribers
        }
    }
}
```

### NotificationService — The Manager
```java
public class NotificationService {
    private final Map<String, Topic> topics;

    public Topic createTopic(String name) {
        Topic topic = new Topic(name);
        topics.put(name, topic);
        return topic;
    }

    public void subscribe(String topicName, User user) {
        Topic topic = topics.get(topicName);
        if (topic != null) topic.subscribe(user);
    }

    public void publish(String topicName, String message) {
        Topic topic = topics.get(topicName);
        if (topic != null) topic.publish(message);
    }
}
```

---

## Part 4: Data Flow — Publishing an Event

```
1. Setup:
   - Create topic "order-updates"
   - Alice subscribes (Email + Push)
   - Bob subscribes (SMS only)

2. Event: service.publish("order-updates", "Order #1234 shipped!")

3. Topic.publish():
   → Loop through subscribers: [Alice, Bob]
   
   → Alice.notify("Order #1234 shipped!")
     → Loop through Alice's channels: [EmailChannel, PushChannel]
       → EmailChannel.send("alice@mail.com", "Order #1234 shipped!")
       → PushChannel.send("Alice", "Order #1234 shipped!")
   
   → Bob.notify("Order #1234 shipped!")
     → Loop through Bob's channels: [SMSChannel]
       → SMSChannel.send("+1-222", "Order #1234 shipped!")

4. Output:
   📧 EMAIL to alice@mail.com: Order #1234 shipped!
   🔔 PUSH to Alice: Order #1234 shipped!
   📱 SMS to +1-222: Order #1234 shipped!
```

---

## Part 5: Class Diagram

```
┌───────────────────┐         ┌──────────────────────┐
│NotificationChannel│◄────────│     EmailChannel     │
│   (interface)     │         ├──────────────────────┤
│                   │◄────────│      SMSChannel      │
│ +send()           │         ├──────────────────────┤
│ +getChannelType() │◄────────│PushNotificationChannel│
└─────────┬─────────┘         └──────────────────────┘
          │ has-many
┌─────────▼──────────┐         ┌─────────────────────┐
│       User         │         │        Topic         │
│ ──────────────     │ N ←───► │  ─────────────────  │
│ name, email, phone │ subscribe│ name                │
│ preferredChannels  │         │ subscribers: List    │
│ ──────────────     │         │ ─────────────────── │
│ +notify(message)   │         │ +subscribe(user)    │
│ +addChannel()      │         │ +publish(message)   │
└────────────────────┘         └──────────┬──────────┘
                                          │ has-many
                               ┌──────────▼──────────┐
                               │NotificationService  │
                               │ ─────────────────── │
                               │ topics: Map          │
                               │ +createTopic()       │
                               │ +subscribe()         │
                               │ +publish()           │
                               └─────────────────────┘
```

---

## Part 6: Follow-Up Questions

| Question | Answer |
|---|---|
| How to handle failures (email server down)? | Add retry logic with exponential backoff. Use a message queue (e.g., Kafka, RabbitMQ) for reliability. |
| How to prioritize channels? | Sort `preferredChannels` by priority. Try the first; if it fails, fall back to the next. |
| How to handle millions of subscribers? | Use async processing: publish event to a message queue, workers send notifications in batches. |
| How to add notification preferences (e.g., "don't notify at night")? | Add a `NotificationPreference` class with time windows, DND mode, etc. Check before sending. |
| How to avoid duplicate notifications? | Track sent notifications with `(userId, messageId)` dedup set. |

---

## Part 7: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Observer** | Topic-User: subscribers get notified when events are published |
| **Strategy** | NotificationChannel: swappable delivery mechanism |
| **SRP** | Each channel, User, Topic has one clear responsibility |
| **OCP** | Add new channels without modifying existing code |
| **Pub-Sub** | Variation of Observer: publishers don't know subscribers |

---

📁 **Source code:** `src/notification/`

