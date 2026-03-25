package figma;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite Pattern: A Group is-a Shape that contains other Shapes.
 */
public class Group extends Shape {
    private final List<Shape> children;

    public Group(double x, double y) {
        super(x, y, 0, 0, "none");
        this.children = new ArrayList<>();
    }

    public void addChild(Shape shape) { children.add(shape); }
    public void removeChild(Shape shape) { children.remove(shape); }
    public List<Shape> getChildren() { return children; }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);
        for (Shape child : children) {
            child.move(dx, dy);
        }
    }

    @Override
    public String getType() { return "Group"; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Group#" + getId() + " [\n");
        for (Shape child : children) {
            sb.append("    ").append(child).append("\n");
        }
        sb.append("  ]");
        return sb.toString();
    }
}

