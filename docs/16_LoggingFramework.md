# Problem 16: Logging Framework

| Pattern | Why |
|---|---|
| **Singleton** | One Logger instance globally |
| **Chain of Responsibility** | Log message passes through a chain of handlers |

## Chain of Responsibility — How It Works
```
LogMessage("ERROR: disk full")
    ↓
ConsoleHandler (level=DEBUG)  → severity ≥ DEBUG? YES → print to console
    ↓ (pass to next)
FileHandler (level=ERROR)     → severity ≥ ERROR? YES → write to file
    ↓ (no next)
Done.
```

Each handler decides: "Am I qualified to handle this?" If yes, process it. Either way, pass to next.

```java
abstract class LogHandler {
    LogHandler nextHandler;
    
    void handle(LogMessage msg) {
        if (msg.level >= myLevel) write(msg);  // process if qualified
        if (nextHandler != null) nextHandler.handle(msg);  // always pass along
    }
}
```

## Log Levels
```
DEBUG (0) < INFO (1) < WARN (2) < ERROR (3) < FATAL (4)
```

## Singleton Logger
```java
Logger logger = Logger.getInstance();
logger.info("Server started");
logger.error("Connection refused");
```

### Double-Checked Locking
```java
private static volatile Logger instance;
public static Logger getInstance() {
    if (instance == null) {
        synchronized (Logger.class) {
            if (instance == null) instance = new Logger("ROOT");
        }
    }
    return instance;
}
```
Thread-safe AND avoids synchronization overhead after first creation.

📁 `src/logger/`

