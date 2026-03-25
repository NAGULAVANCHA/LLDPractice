package notification;

public class SMSChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String message) {
        System.out.println("  📱 SMS to " + recipient + ": " + message);
    }

    @Override
    public String getChannelType() { return "SMS"; }
}

