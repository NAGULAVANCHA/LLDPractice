package figma;

import java.util.ArrayList;
import java.util.List;

public class Canvas {
    private final String name;
    private final List<Shape> shapes;
    private final CommandHistory history;

    public Canvas(String name) {
        this.name = name;
        this.shapes = new ArrayList<>();
        this.history = new CommandHistory();
    }

    // Command-based operations (with undo/redo support)
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

    // Direct operations (called by commands internally)
    void addShapeDirect(Shape shape) { shapes.add(shape); }
    void removeShapeDirect(Shape shape) { shapes.remove(shape); }

    public List<Shape> getShapes() { return shapes; }
    public String getName() { return name; }

    public void display() {
        System.out.println("\n=== Canvas: " + name + " ===");
        if (shapes.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Shape s : shapes) {
                System.out.println("  " + s);
            }
        }
        System.out.println("========================");
    }
}

