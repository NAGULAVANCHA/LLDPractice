package pubsub;

public class Producer {
    private final String name;

    public Producer(String name) {
        this.name = name;
    }

    public void send(PubSubTopic topic, String content) {
        System.out.println(name + " producing to [" + topic.getName() + "]:");
        topic.publish(new Message(content));
    }
}

