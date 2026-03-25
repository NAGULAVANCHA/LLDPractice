package logger;

/**
 * Chain of Responsibility: Each handler processes if level >= its threshold,
 * then passes to the next handler.
 */
public abstract class LogHandler {
    private final LogLevel level;
    private LogHandler nextHandler;

    public LogHandler(LogLevel level) {
        this.level = level;
    }

    public LogHandler setNext(LogHandler next) {
        this.nextHandler = next;
        return next;
    }

    public void handle(LogMessage message) {
        if (message.getLevel().getSeverity() >= level.getSeverity()) {
            write(message);
        }
        if (nextHandler != null) {
            nextHandler.handle(message);
        }
    }

    protected abstract void write(LogMessage message);
}

