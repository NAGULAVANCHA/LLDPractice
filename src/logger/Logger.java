package logger;

/**
 * Singleton Logger — entry point for all logging.
 */
public class Logger {
    private static volatile Logger instance;
    private LogHandler handlerChain;
    private LogLevel minLevel;
    private final String name;

    private Logger(String name) {
        this.name = name;
        this.minLevel = LogLevel.DEBUG;
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger("ROOT");
                    // Default chain: Console (all levels)
                    instance.handlerChain = new ConsoleHandler(LogLevel.DEBUG);
                }
            }
        }
        return instance;
    }

    // For testing: reset singleton
    public static void resetInstance() { instance = null; }

    public void setHandlerChain(LogHandler chain) {
        this.handlerChain = chain;
    }

    public void setLevel(LogLevel level) {
        this.minLevel = level;
    }

    public void log(LogLevel level, String message) {
        if (level.getSeverity() < minLevel.getSeverity()) return;
        LogMessage msg = new LogMessage(level, message, name);
        if (handlerChain != null) handlerChain.handle(msg);
    }

    public void debug(String msg) { log(LogLevel.DEBUG, msg); }
    public void info(String msg)  { log(LogLevel.INFO, msg); }
    public void warn(String msg)  { log(LogLevel.WARN, msg); }
    public void error(String msg) { log(LogLevel.ERROR, msg); }
    public void fatal(String msg) { log(LogLevel.FATAL, msg); }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== Logging Framework Demo ===\n");

        Logger.resetInstance();
        Logger logger = Logger.getInstance();

        System.out.println("--- All levels (default) ---");
        logger.debug("Initializing app...");
        logger.info("App started");
        logger.warn("Disk space low");
        logger.error("NullPointerException");
        logger.fatal("System crash!");

        // Chain: Console for ALL, File for ERROR+
        System.out.println("\n--- Custom chain: Console(ALL) + File(ERROR+) ---");
        ConsoleHandler consoleHandler = new ConsoleHandler(LogLevel.DEBUG);
        FileHandler fileHandler = new FileHandler(LogLevel.ERROR, "error.log");
        consoleHandler.setNext(fileHandler);
        logger.setHandlerChain(consoleHandler);

        logger.debug("Debug message");
        logger.info("Info message");
        logger.error("Critical error!");
        logger.fatal("System down!");

        // Set minimum level to WARN
        System.out.println("\n--- Set minimum level to WARN ---");
        logger.setLevel(LogLevel.WARN);
        logger.debug("This won't show");
        logger.info("This won't show either");
        logger.warn("This will show");
        logger.error("This too");
    }
}

