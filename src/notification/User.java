package notification;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String name;
    private final String email;
    private final String phone;
    private final List<NotificationChannel> preferredChannels;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferredChannels = new ArrayList<>();
    }

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

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}

