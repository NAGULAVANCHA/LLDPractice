# Problem 7: Collaborative Design Tool (Figma-like) — Complete Guide

## Patterns Used
| Pattern | Purpose |
|---|---|
| **Command** | Undo/redo support |
| **Composite** | Groups of shapes (Group IS-A Shape, HAS Shapes) |
| **Observer** | Real-time collaboration notifications |
| **Prototype** | Clone/duplicate shapes |

---

## Command Pattern — Undo/Redo

Wrap every action in a command object that knows how to **execute** AND **reverse** itself.

```java
interface Command { void execute(); void undo(); }

class AddShapeCommand    { execute → shapes.add(s);    undo → shapes.remove(s); }
class MoveShapeCommand   { execute → shape.move(dx,dy); undo → shape.move(-dx,-dy); }
class DeleteShapeCommand { execute → shapes.remove(s);  undo → shapes.add(s); }
```

### Undo/Redo Stacks
```java
class CommandHistory {
    Stack<Command> undoStack, redoStack;
    
    void executeCommand(cmd) { cmd.execute(); undoStack.push(cmd); redoStack.clear(); }
    void undo() { cmd = undoStack.pop(); cmd.undo(); redoStack.push(cmd); }
    void redo() { cmd = redoStack.pop(); cmd.execute(); undoStack.push(cmd); }
}
```
`redoStack.clear()` on new action because a new action invalidates the redo history.

---

## Composite Pattern — Groups

Treat individual shapes and groups **the same way**:
```java
class Group extends Shape {
    List<Shape> children;
    void move(dx, dy) {
        super.move(dx, dy);
        for (Shape child : children) child.move(dx, dy); // recursive!
    }
}
```

---

## Prototype Pattern — Clone
```java
Shape copy = original.clone(); // duplicate without knowing exact type
```

---

## Canvas Design
```java
// Public methods go through commands (trackable for undo/redo)
void addShape(s)    { history.executeCommand(new AddShapeCommand(this, s)); }
// Direct methods called BY commands internally
void addShapeDirect(s) { shapes.add(s); }
```

## CollaborationSession (Observer)
```java
void broadcast(msg) { for (user : activeUsers) user.receiveUpdate(msg); }
```

---

## Follow-Ups
| Question | Answer |
|---|---|
| Concurrent edits? | OT or CRDT |
| Layers? | Layer class containing shapes |
| Export? | Visitor Pattern |

📁 **Source code:** `src/figma/`
