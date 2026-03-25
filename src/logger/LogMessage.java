package logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogMessage {
    private final LogLevel level;
    private final String message;
    private final String source;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogMessage(LogLevel level, String message, String source) {
        this.level = level;
        this.message = message;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return "[" + timestamp.format(FMT) + "] [" + level + "] [" + source + "] " + message;
    }
}

