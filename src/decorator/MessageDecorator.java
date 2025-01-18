package decorator;

public abstract class MessageDecorator implements Message {
    protected Message message;

    public MessageDecorator(Message message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message.getMessage();
    }
}
