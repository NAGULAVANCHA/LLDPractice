package logger;

public class ConsoleHandler extends LogHandler {
    public ConsoleHandler(LogLevel level) { super(level); }

    @Override
    protected void write(LogMessage message) {
        System.out.println("  [CONSOLE] " + message);
    }
}

