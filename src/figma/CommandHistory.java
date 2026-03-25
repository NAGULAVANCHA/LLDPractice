package figma;

import java.util.Stack;

/**
 * Manages undo/redo stacks.
 */
public class CommandHistory {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear(); // new action invalidates redo history
        System.out.println("  ✓ " + cmd.describe());
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("  Nothing to undo!");
            return;
        }
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
        System.out.println("  ↩ Undo: " + cmd.describe());
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("  Nothing to redo!");
            return;
        }
        Command cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
        System.out.println("  ↪ Redo: " + cmd.describe());
    }
}

