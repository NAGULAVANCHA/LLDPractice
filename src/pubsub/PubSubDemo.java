package pubsub;

public class PubSubDemo {
    public static void main(String[] args) {
        System.out.println("=== Pub-Sub Message Queue Demo ===\n");

        // Create topics
        PubSubTopic orders = new PubSubTopic("orders");
        PubSubTopic analytics = new PubSubTopic("analytics");

        // Create consumers (push-based subscription)
        Consumer orderProcessor = new Consumer("OrderProcessor");
        Consumer analyticsEngine = new Consumer("AnalyticsEngine");
        Consumer logger = new Consumer("Logger");

        orders.addSubscriber(orderProcessor);
        orders.addSubscriber(logger);
        analytics.addSubscriber(analyticsEngine);

        // Produce messages
        Producer orderService = new Producer("OrderService");
        Producer clickTracker = new Producer("ClickTracker");

        System.out.println("\n--- Publishing ---");
        orderService.send(orders, "Order #1001 placed");
        orderService.send(orders, "Order #1002 placed");
        clickTracker.send(analytics, "User clicked Buy button");
        orderService.send(orders, "Order #1001 shipped");

        // Polling mode (consumer pulls)
        System.out.println("\n--- Polling mode (new consumer) ---");
        Consumer latecomer = new Consumer("Latecomer");
        latecomer.poll(orders, 10); // reads all existing messages

        // Reset offset and replay
        System.out.println("\n--- Replay from offset 0 ---");
        latecomer.resetOffset("orders", 0);
        latecomer.poll(orders, 2); // replay first 2 messages
    }
}

