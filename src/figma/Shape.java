package figma;

/**
 * COLLABORATIVE DESIGN TOOL (Figma-like) LLD
 * =============================================
 * Key Concepts:
 *  - Composite Pattern:  Shapes can be grouped (Group is-a Shape, has Shapes)
 *  - Command Pattern:    Undo/Redo support for all operations
 *  - Observer Pattern:   Real-time collaboration (notify other users of changes)
 *  - Prototype Pattern:  Clone/duplicate shapes
 *
 * Classes:
 *  Shape (abstract)     -> Base: Rectangle, Circle, Text
 *  Group                -> Composite of multiple shapes
 *  Command (interface)  -> AddShape, MoveShape, DeleteShape, GroupShapes
 *  CommandHistory       -> Manages undo/redo stack
 *  Canvas               -> The drawing surface holding all shapes
 *  CollaborationSession -> Multiple users editing same canvas
 *  DesignUser           -> A collaborating user with a cursor position
 */
public abstract class Shape implements Cloneable {
    private static int idCounter = 0;
    private final int id;
    private double x, y;
    private double width, height;
    private String color;

    public Shape(double x, double y, double width, double height, String color) {
        this.id = ++idCounter;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public abstract String getType();

    @Override
    public Shape clone() {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getType() + "#" + id + " at (" + x + "," + y + ") " +
                width + "x" + height + " [" + color + "]";
    }
}

