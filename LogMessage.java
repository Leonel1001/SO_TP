public class LogMessage {
    private final String message;
    private final Middleware source;

    public LogMessage(String message, Middleware source) {
        this.message = message;
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public Middleware getSource() {
        return source;
    }

}
