# Problem 16: Logging Framework — Complete Guide

---

## Part 1: Understanding the Problem

A Log4j-like logging framework with configurable log levels and multiple output targets.

### Requirements
- ✓ Log levels: DEBUG < INFO < WARN < ERROR < FATAL
- ✓ Multiple output handlers (console, file, database)
- ✓ Chain of Responsibility: message flows through handler chain
- ✓ Singleton Logger
- ✓ Configurable minimum level

---

## Part 2: The Key Insight — Chain of Responsibility

### Real World Analogy
**Customer support escalation:** You call support. The first agent (Level 1) checks if they can handle it. If it's too complex, they pass it to Level 2, who passes it to Level 3 if needed. Each level DECIDES whether to handle AND passes to the next.

### In Our Logger
```
LogMessage("ERROR: disk full")
    ↓
ConsoleHandler (minLevel=DEBUG)
  → Is ERROR ≥ DEBUG? YES → print to console
    ↓ (pass to next handler)
FileHandler (minLevel=ERROR)
  → Is ERROR ≥ ERROR? YES → write to file
    ↓ (no next handler)
Done.

LogMessage("DEBUG: cache hit")
    ↓
ConsoleHandler (minLevel=DEBUG)
  → Is DEBUG ≥ DEBUG? YES → print to console
    ↓
FileHandler (minLevel=ERROR)
  → Is DEBUG ≥ ERROR? NO → skip
    ↓ (no next handler)
Done.
```

**Result:** DEBUG messages go to console only. ERROR messages go to BOTH console AND file.

---

## Part 3: The Code — Explained

### LogLevel — Enum with Severity
```java
public enum LogLevel {
    DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);
    private final int severity;
}
```

### LogHandler — Abstract Chain Link
```java
public abstract class LogHandler {
    private final LogLevel level;      // minimum level this handler processes
    private LogHandler nextHandler;    // next in chain

    public LogHandler setNext(LogHandler next) {
        this.nextHandler = next;
        return next;                    // returns next for fluent chaining
    }

    public void handle(LogMessage message) {
        if (message.getLevel().getSeverity() >= level.getSeverity()) {
            write(message);             // process if qualified
        }
        if (nextHandler != null) {
            nextHandler.handle(message); // ALWAYS pass to next
        }
    }

    protected abstract void write(LogMessage message);  // subclass implements
}
```

**Key: `handle()` does TWO things:**
1. Processes the message IF level is high enough
2. **Always** passes to next handler (unlike some CoR where you stop after handling)

### Concrete Handlers
```java
public class ConsoleHandler extends LogHandler {
    protected void write(LogMessage msg) {
        System.out.println("[CONSOLE] " + msg);
    }
}

public class FileHandler extends LogHandler {
    private final String fileName;
    private final List<String> fileContent = new ArrayList<>();

    protected void write(LogMessage msg) {
        fileContent.add(msg.toString());  // simulate file write
        System.out.println("[FILE:" + fileName + "] " + msg);
    }
}
```

### Logger — Singleton with Chain Setup
```java
public class Logger {
    private static volatile Logger instance;
    private LogHandler handlerChain;
    private LogLevel minLevel;

    // Double-checked locking — thread-safe + efficient
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger("ROOT");
                    instance.handlerChain = new ConsoleHandler(LogLevel.DEBUG);
                }
            }
        }
        return instance;
    }

    public void log(LogLevel level, String message) {
        if (level.getSeverity() < minLevel.getSeverity()) return;  // below threshold
        LogMessage msg = new LogMessage(level, message, name);
        if (handlerChain != null) handlerChain.handle(msg);
    }

    // Convenience methods
    public void debug(String msg) { log(LogLevel.DEBUG, msg); }
    public void info(String msg)  { log(LogLevel.INFO, msg); }
    public void warn(String msg)  { log(LogLevel.WARN, msg); }
    public void error(String msg) { log(LogLevel.ERROR, msg); }
    public void fatal(String msg) { log(LogLevel.FATAL, msg); }
}
```

### Building the Chain
```java
ConsoleHandler console = new ConsoleHandler(LogLevel.DEBUG);   // all levels
FileHandler file = new FileHandler(LogLevel.ERROR, "error.log"); // error+ only
console.setNext(file);  // chain: console → file

logger.setHandlerChain(console);
```

---

## Part 4: Data Flow

```
logger.setLevel(LogLevel.WARN);
Chain: ConsoleHandler(DEBUG) → FileHandler(ERROR)

logger.debug("cache hit")     → BLOCKED by minLevel (DEBUG < WARN)
logger.info("user login")     → BLOCKED by minLevel (INFO < WARN)
logger.warn("disk 90%")       → Console ✓, File ✗ (WARN < ERROR)
logger.error("connection lost")→ Console ✓, File ✓ (ERROR ≥ ERROR)
logger.fatal("system crash")  → Console ✓, File ✓ (FATAL ≥ ERROR)
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Database handler? | Create `DatabaseHandler extends LogHandler`. `write()` inserts into a DB table. |
| Async logging? | Wrap handler in `AsyncHandler` that uses a `BlockingQueue` + background thread. |
| Log rotation? | `FileHandler` tracks file size. When it exceeds limit, archive and start new file. |
| Structured logging (JSON)? | Create `JsonFormatter` and use it in `write()` instead of `toString()`. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Chain of Responsibility** | Log message flows through handler chain |
| **Singleton** | One Logger instance globally |
| **Template Method** | Base `handle()` calls abstract `write()` |
| **OCP** | Add new handlers without modifying Logger |
| **Double-Checked Locking** | Thread-safe singleton without synchronization overhead |

---

📁 **Source code:** `src/logger/`
