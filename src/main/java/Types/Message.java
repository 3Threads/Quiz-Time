package Types;


public class Message {
    private final int from;
    private final int to;
    private final String message;

    public Message(int from, int to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }
}
