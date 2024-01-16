public class LogMessage {
    private String message;
    private Object source;

    public LogMessage(String message, Object source) {
        this.message = message;
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public Object getSource() {
        return source;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
