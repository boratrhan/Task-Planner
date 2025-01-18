package decorator;

import java.util.List;

public class NotificationMessageDecorator extends MessageDecorator {
    private List<String> notifications;

    public NotificationMessageDecorator(Message message, List<String> notifications) {
        super(message);
        this.notifications = notifications;
    }

    @Override
    public String getMessage() {
        String baseMessage = super.getMessage();
        if (notifications != null && !notifications.isEmpty()) {
            StringBuilder notificationBuilder = new StringBuilder(baseMessage);
            notificationBuilder.append("\nNotifications:");
            for (String notification : notifications) {
                notificationBuilder.append("\n- ").append(notification);
            }
            return notificationBuilder.toString();
        }
        return baseMessage;
    }
}
