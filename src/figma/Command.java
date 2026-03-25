package figma;

/**
 * Command Pattern: Encapsulates an operation as an object
 * so we can undo/redo it.
 */
public interface Command {
    void execute();
    void undo();
    String describe();
}

