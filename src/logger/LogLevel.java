package logger;

/**
 * LOGGING FRAMEWORK LLD (Log4j-like)
 * =====================================
 * Key Concepts:
 *  - Singleton:            Logger.getInstance()
 *  - Chain of Responsibility: Log levels form a chain (DEBUG -> INFO -> WARN -> ERROR -> FATAL)
 *  - Strategy Pattern:     Different output targets (Console, File, DB)
 *  - OCP:                  Add new log levels or outputs without modifying core
 *
 * Interview Points:
 *  - Chain of Responsibility: Each handler decides to process or pass to next
 *  - Log levels have hierarchy: DEBUG < INFO < WARN < ERROR < FATAL
 *  - Setting level to WARN means only WARN, ERROR, FATAL are logged
 *  - Multiple appenders (console + file) can work simultaneously
 *  - Thread-safe singleton
 */
public enum LogLevel {
    DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);

    private final int severity;

    LogLevel(int severity) { this.severity = severity; }

    public int getSeverity() { return severity; }
}

