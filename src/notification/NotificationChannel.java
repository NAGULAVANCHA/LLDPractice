package notification;

/**
 * NOTIFICATION SYSTEM LLD
 * ========================
 * Key Concepts:
 *  - Observer Pattern: Subscribers get notified when events happen
 *  - Strategy Pattern: Different notification channels (Email, SMS, Push)
 *  - OCP: Add new channels without modifying existing code
 *  - SRP: Each channel handles its own delivery logic
 *
 * Design:
 *  NotificationChannel (Interface) — defines send()
 *  EmailChannel, SMSChannel, PushChannel — concrete channels
 *  Topic — a subject users can subscribe to
 *  User  — subscriber who receives notifications via their channels
 *  NotificationService — manages topics and dispatches notifications
 *
 * Flow:
 *  1. Create topics ("order-updates", "promotions")
 *  2. Users subscribe to topics with preferred channels
 *  3. When an event occurs, all subscribers of that topic get notified
 */
public interface NotificationChannel {
    void send(String recipient, String message);
    String getChannelType();
}

