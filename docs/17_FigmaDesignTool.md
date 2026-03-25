# Problem 7: Collaborative Design Tool (Figma-like) — Complete Guide

---

## Part 1: Understanding the Problem

### What are we building?
A simplified Figma: a collaborative design tool where:
- Users create shapes on a canvas (Rectangle, Circle, TextBox)
- Shapes can be grouped together and moved as one
- All operations support **undo/redo**
- Multiple users can collaborate on the same canvas in real-time

### This Problem Uses FOUR Design Patterns!

| Pattern | Purpose | Real World |
|---|---|---|
| **Command** | Undo/redo support | Ctrl+Z in any editor |
| **Composite** | Groups of shapes (Group IS-A Shape) | Folder containing files & folders |
| **Observer** | Real-time collaboration notifications | Google Docs — see others' edits live |
| **Prototype** | Clone/duplicate shapes | Copy-paste a shape |

---

## 🎯 Design Pattern: Command Pattern

### Problem It Solves
You want to support **undo/redo** for operations. To undo something, you need to **remember** what was done.

### Real World Analogy
**Text Editor:** You type "Hello", then "World". Ctrl+Z removes "World" (undo). Ctrl+Y puts "World" back (redo). The editor doesn't just perform actions — it records them so it can reverse them.

### How It Works

```
1. Wrap every action in a COMMAND OBJECT
2. Command has execute() and undo()
3. Push executed commands onto an UNDO stack
4. To undo: pop from undo stack, call undo(), push onto REDO stack
5. To redo: pop from redo stack, call execute(), push onto UNDO stack
```

### WITHOUT Command Pattern ❌
```java
class Canvas {
    void addShape(Shape s) { shapes.add(s); }
    void moveShape(Shape s, double dx, double dy) { s.move(dx, dy); }
    void deleteShape(Shape s) { shapes.remove(s); }

    // How do you undo? You'd need to track:
    // - What was the LAST action? add? move? delete?
    // - What were the PARAMETERS? which shape? how far?
    // → Massive if/else mess that grows with every new operation!
}
```

### WITH Command Pattern ✅
```java
interface Command {
    void execute();
    void undo();
    String describe();
}

class AddShapeCommand implements Command {
    private final Canvas canvas;
    private final Shape shape;
    void execute() { canvas.addShapeDirect(shape); }
    void undo()    { canvas.removeShapeDirect(shape); }   // exact reverse!
}

class MoveShapeCommand implements Command {
    private final Shape shape;
    private final double dx, dy;
    void execute() { shape.move(dx, dy); }
    void undo()    { shape.move(-dx, -dy); }              // move back!
}

class DeleteShapeCommand implements Command {
    void execute() { canvas.removeShapeDirect(shape); }
    void undo()    { canvas.addShapeDirect(shape); }      // add it back!
}
```

Each command knows how to **do** AND **undo** itself. Self-contained!

### CommandHistory — Undo/Redo Manager

```java
public class CommandHistory {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();  // new action invalidates redo history!
    }

    public void undo() {
        if (undoStack.isEmpty()) { "Nothing to undo!"; return; }
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
    }

    public void redo() {
        if (redoStack.isEmpty()) { "Nothing to redo!"; return; }
        Command cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
    }
}
```

**Why `redoStack.clear()` on new action?**
If you undo 3 times, then do a NEW action, the redo history is gone — you took a new branch.
```
History: A → B → C
Undo twice → redo stack: [B, C]
New action D → redo stack: [] (cleared!)
Now: A → D  (can't redo to B or C anymore)
```

---

## 🎯 Design Pattern: Composite Pattern

### Problem It Solves
Treat **individual objects** and **groups of objects** the **same way**.

### Real World Analogy
**File System:** A File and a Folder both have a name, both can be moved. A Folder CONTAINS Files and other Folders. You can call `getSize()` on both — folder recursively sums its children.

### In Our Design Tool
```
Shape (abstract)
├── Rectangle
├── Circle
├── TextBox
└── Group ← IS a Shape, but CONTAINS other Shapes!
```

```java
public class Group extends Shape {
    private final List<Shape> children = new ArrayList<>();

    public void addChild(Shape shape) { children.add(shape); }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);              // move the group's own position
        for (Shape child : children) {
            child.move(dx, dy);           // move ALL children too!
        }
    }
}
```

**The magic:** When you call `group.move(10, 10)`, it moves ALL shapes inside the group. If a group contains another group, that inner group moves its children too — it's **recursive**.

---

## 🎯 Design Pattern: Prototype Pattern

```java
public abstract class Shape implements Cloneable {
    @Override
    public Shape clone() {
        try { return (Shape) super.clone(); }
        catch (CloneNotSupportedException e) { throw new RuntimeException(e); }
    }
}

// Usage:
Shape original = new Rectangle(10, 10, 100, 50, "blue");
Shape copy = original.clone();  // exact duplicate, don't need to know the type!
```

---

## Part 2: The Code — Key Design Decisions

### Canvas — Public vs Direct Methods
```java
public class Canvas {
    private final List<Shape> shapes;
    private final CommandHistory history;

    // PUBLIC methods go through commands (trackable for undo/redo)
    public void addShape(Shape shape) {
        history.executeCommand(new AddShapeCommand(this, shape));
    }
    public void moveShape(Shape shape, double dx, double dy) {
        history.executeCommand(new MoveShapeCommand(shape, dx, dy));
    }
    public void deleteShape(Shape shape) {
        history.executeCommand(new DeleteShapeCommand(this, shape));
    }

    public void undo() { history.undo(); }
    public void redo() { history.redo(); }

    // DIRECT methods (called by commands internally — NOT recorded)
    void addShapeDirect(Shape shape)    { shapes.add(shape); }
    void removeShapeDirect(Shape shape) { shapes.remove(shape); }
}
```

**Why two sets of methods?**
- `addShape()` → goes through Command → records in history → undoable
- `addShapeDirect()` → directly modifies list → called BY commands during execute/undo
- Without this separation, calling undo would try to record ANOTHER command → infinite loop!

### CollaborationSession — Observer Pattern
```java
public class CollaborationSession {
    private final Canvas canvas;
    private final List<DesignUser> activeUsers;

    public void join(DesignUser user) {
        activeUsers.add(user);
        broadcast(user.getName() + " joined the session");
    }

    public void leave(DesignUser user) {
        activeUsers.remove(user);
        broadcast(user.getName() + " left the session");
    }

    public void broadcast(String message) {
        for (DesignUser user : activeUsers) {
            // each user receives the update
        }
    }
}
```

---

## Part 3: Data Flow — Undo/Redo Example

```
1. canvas.addShape(rect)
   → executeCommand(AddShapeCommand)
   → AddShapeCommand.execute() → shapes.add(rect)
   → undoStack: [AddShapeCmd],  redoStack: []

2. canvas.moveShape(rect, 50, 20)
   → executeCommand(MoveShapeCommand)
   → MoveShapeCommand.execute() → rect.move(50, 20)
   → undoStack: [AddCmd, MoveCmd],  redoStack: []

3. canvas.undo()
   → pop MoveCmd from undoStack
   → MoveCmd.undo() → rect.move(-50, -20)  ← moved back!
   → undoStack: [AddCmd],  redoStack: [MoveCmd]

4. canvas.redo()
   → pop MoveCmd from redoStack
   → MoveCmd.execute() → rect.move(50, 20)  ← moved again!
   → undoStack: [AddCmd, MoveCmd],  redoStack: []
```

---

## Part 4: Class Diagram

```
          ┌─────────────┐
          │   Command    │ ← interface
          │  +execute()  │
          │  +undo()     │
          └──────▲───────┘
       ┌─────────┼──────────┐
       │         │          │
┌──────┴───┐ ┌───┴─────┐ ┌──┴──────────┐
│AddShape  │ │MoveShape│ │DeleteShape  │
│Command   │ │Command  │ │Command      │
└──────────┘ └─────────┘ └─────────────┘

┌────────────────┐ uses    ┌────────────────┐
│     Canvas     │────────→│ CommandHistory  │
│  +addShape()   │         │  undoStack      │
│  +moveShape()  │         │  redoStack      │
│  +undo()       │         │  +executeCmd()  │
│  +redo()       │         │  +undo()        │
└────────────────┘         │  +redo()        │
                           └────────────────┘

┌──────────────┐
│ Shape (abs)  │ implements Cloneable
├──────────────┤
│ Rectangle    │
│ Circle       │
│ TextBox      │
│ Group        │ ← Composite (has List<Shape>)
└──────────────┘

┌─────────────────────┐      ┌────────────┐
│CollaborationSession │──────│ DesignUser │
│  +join() +leave()   │ many │ name       │
│  +broadcast()       │      │ cursorX, Y │
└─────────────────────┘      └────────────┘
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| How to handle concurrent edits? | Use **OT** (Operational Transformation) or **CRDT** (Conflict-free Replicated Data Types). Each user has a local copy; changes are merged. |
| How to add layers? | Create a `Layer` class containing shapes. Canvas has an ordered list of Layers. Render in layer order (back to front). |
| How to add resize? | Create `ResizeShapeCommand` storing old and new dimensions. `undo()` restores old dimensions. |
| Long undo history? | Set max history size or save periodic snapshots (checkpoints). |
| Export to SVG/PNG? | **Visitor Pattern**: create a `ShapeVisitor` interface. One visitor for SVG, another for PNG. Each shape accepts the visitor. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Command** | All canvas operations → undo/redo support |
| **Composite** | Group IS-A Shape, HAS Shapes → recursive move/resize |
| **Observer** | CollaborationSession → notify all users of changes |
| **Prototype** | Shape.clone() → duplicate shapes without knowing concrete type |
| **SRP** | Canvas manages shapes, CommandHistory manages undo/redo stacks |
| **OCP** | Add new commands (ResizeCommand) without changing Canvas code |

---

📁 **Source code:** `src/figma/`
