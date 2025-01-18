package decorator;

import java.time.LocalDate;

public class BirthdayMessageDecorator extends MessageDecorator {
    private static final String USER_BIRTHDAY = "2025-01-17";

    public BirthdayMessageDecorator(Message message) {
        super(message);
    }

    @Override
    public String getMessage() {
        String baseMessage = super.getMessage();
        String today = LocalDate.now().toString();
        if (today.equals(USER_BIRTHDAY)) {
            return baseMessage + "\n Happy Birthday! ";
        }
        return baseMessage;
    }
}
