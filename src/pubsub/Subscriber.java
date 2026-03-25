package pubsub;

public interface Subscriber {
    void onMessage(String topic, Message message);
    String getName();
}

